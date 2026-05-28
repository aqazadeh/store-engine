package az.kon.academy.event.handler.autoconfiguration;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.BaseEventHandler;
import az.kon.academy.event.handler.DefaultDomainEventRegistry;
import az.kon.academy.event.handler.DomainEventPublisherPipeline;
import az.kon.academy.event.handler.EventHandlerRegistry;
import az.kon.academy.event.handler.autoconfiguration.annotation.EventHandler;
import az.kon.academy.event.handler.autoconfiguration.annotation.EvenHandlerInterceptor;
import az.kon.academy.event.handler.autoconfiguration.interceptor.EventHandlerLoggingInterceptor;
import az.kon.academy.event.handler.autoconfiguration.interceptor.EventHandlerMetricInterceptor;
import az.kon.academy.event.handler.autoconfiguration.metrics.DefaultEventHandlerMonitor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptorRegistry;
import az.kon.academy.event.handler.metric.EventHandlerMonitor;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.GenericTypeResolver;

public class EventHandlerAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(EventHandlerAutoConfiguration.class);

    private final ApplicationContext applicationContext;

    public EventHandlerAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnBooleanProperty(prefix = "store-engine.event.handler.metrics", name = "enabled")
    public EventHandlerMonitor eventHandlerMonitor(MeterRegistry meterRegistry) {
        return new DefaultEventHandlerMonitor(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(EventHandlerMonitor.class)
    public EventHandlerMonitor noopEventHandlerMonitor() {
        return EventHandlerMonitor.NOOP;
    }

    @Bean
    @ConditionalOnMissingBean(EventHandlerRegistry.class)
    public EventHandlerRegistry eventHandlerRegistry() {
        return new DefaultDomainEventRegistry();
    }

    @Bean
    public DomainEventPublisherPipeline eventBus(EventHandlerRegistry registry, EventHandlerMonitor monitor){
        DomainEventPublisherPipeline pipeline = new DomainEventPublisherPipeline(registry, monitor);
        this.registerEventHandlers(registry);
        this.registerEventHandlerInterceptors(pipeline);
        return pipeline;
    }

    private void registerEventHandlerInterceptors(EventHandlerInterceptorRegistry registry) {
        var interceptors = applicationContext.getBeansWithAnnotation(EvenHandlerInterceptor.class);

        interceptors.forEach((beanName, interceptor) -> {
            if (interceptor instanceof EventHandlerInterceptor ei) {
                logger.debug("Registering event handler interceptor: {}", interceptor.getClass().getSimpleName());
                registry.registerInterceptor(ei);
            } else {
                throw new RuntimeException("Interceptor is not a EventHandlerInterceptor: " + interceptor.getClass().getName());
            }
        });

        logger.info("Registered {} event handler interceptors: {}", interceptors.size(),
                interceptors.values().stream().map(o -> o.getClass().getSimpleName()).toList());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerEventHandlers(EventHandlerRegistry registry) {
        var handlers = applicationContext.getBeansWithAnnotation(EventHandler.class);

        handlers.forEach((beanName, handler) -> {
            if (handler instanceof BaseEventHandler<?>) {
                Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                        handler.getClass(),
                        BaseEventHandler.class
                );

                if (generics != null && generics.length >= 1 && AbstractEvent.class.isAssignableFrom(generics[0])) {
                    Class<? extends AbstractEvent> eventType = (Class<? extends AbstractEvent>) generics[0];
                    logger.debug("Registering event handler for event type: {} with handler: {}",
                            eventType.getSimpleName(),
                            handler.getClass().getSimpleName());

                    registry.registerHandler((BaseEventHandler) handler, eventType);
                } else {
                    throw new RuntimeException("Could not resolve event type for handler: " + handler.getClass().getName());
                }
            } else {
                throw new RuntimeException("Bean " + beanName + " is annotated with @EventHandler but does not implement BaseEventHandler");
            }
        });

        logger.info("Registered {} event handlers {}", handlers.size(),
                handlers.values().stream().map(o -> o.getClass().getSimpleName()).toList());
    }
}
