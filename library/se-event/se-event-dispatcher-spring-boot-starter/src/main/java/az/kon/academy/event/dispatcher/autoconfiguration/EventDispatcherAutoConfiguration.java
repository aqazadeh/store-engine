package az.kon.academy.event.dispatcher.autoconfiguration;

import az.kon.academy.event.dispatcher.DefaultEventDispatcher;
import az.kon.academy.event.dispatcher.EventDispatchMonitor;
import az.kon.academy.event.dispatcher.EventDispatchStrategyRegistry;
import az.kon.academy.event.dispatcher.EventDispatcher;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventDispatcherAutoConfiguration {

    @Bean
    @ConditionalOnClass(MeterRegistry.class)
    public EventDispatchMonitor eventDispatchMonitor(MeterRegistry meterRegistry) {
        return new DefaultEventDispatchMonitor(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean(EventDispatchMonitor.class)
    public EventDispatchMonitor noopEventDispatchMonitor() {
        return EventDispatchMonitor.NOOP;
    }

    @Bean
    public EventDispatchStrategyRegistry eventDispatchStrategyRegistry() {
        return new EventDispatchStrategyRegistry();
    }

    @Bean
    @ConditionalOnMissingBean(EventDispatcher.class)
    public EventDispatcher eventDispatcher(EventDispatchStrategyRegistry registry, EventDispatchMonitor monitor) {
        return new DefaultEventDispatcher(registry, monitor);
    }
}
