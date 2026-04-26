package az.kon.academy.event.dispatcher.autoconfiguration;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.dispatcher.EventDispatchStrategy;
import az.kon.academy.event.dispatcher.EventDispatchStrategyRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class EventDispatchStrategyAutoConfiguration {
    private final ApplicationContext applicationContext;
    private final EventDispatchStrategyRegistry registry;
    private final EventDispatchStrategyProperties eventDispatchStrategyProperties;

    public EventDispatchStrategyAutoConfiguration(ApplicationContext applicationContext,
                                                  EventDispatchStrategyRegistry registry,
                                                  EventDispatchStrategyProperties eventDispatchStrategyProperties) {
        this.registry = registry;
        this.applicationContext = applicationContext;
        this.eventDispatchStrategyProperties = eventDispatchStrategyProperties;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        Map<String, EventDispatchStrategy> strategies = this.applicationContext.getBeansOfType(EventDispatchStrategy.class);
        this.eventDispatchStrategyProperties.getStrategies().forEach((eventClass, strategyName) -> {

            if (!strategies.containsKey(strategyName)) {
                throw new RuntimeException("No EventDispatchStrategy bean found with name: " + strategyName + ". Allowed strategies: " + strategies.keySet());
            }
            if (!AbstractEvent.class.isAssignableFrom(eventClass)) {
                throw new RuntimeException("Event type must extend AbstractEvent: " + eventClass.getName());
            }
            EventDispatchStrategy strategy = strategies.get(strategyName);
            registry.register((Class<? extends AbstractEvent>) eventClass, strategy);
        });
    }
}
