package az.kon.academy.event.dispatcher.autoconfiguration;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.dispatcher.EventDispatchStrategy;
import az.kon.academy.event.dispatcher.EventDispatchStrategyRegistry;
import az.kon.academy.event.dispatcher.autoconfiguration.data.EventDispatchStrategyProperties;
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
    public void init() {
        Map<String, EventDispatchStrategy> strategies = this.applicationContext.getBeansOfType(EventDispatchStrategy.class);
        this.eventDispatchStrategyProperties
                .getStrategies()
                .forEach(entry -> {
                    if (!strategies.containsKey(entry.getStrategy().getName())) {
                        throw new RuntimeException("No EventDispatchStrategy bean found with name: "
                                + entry.getStrategy().getSimpleName() + ". Allowed strategies: " + strategies.keySet());
                    }
                    if (!AbstractEvent.class.isAssignableFrom(entry.getEvent())) {
                        throw new RuntimeException("Event type must extend AbstractEvent: " + entry.getEvent().getSimpleName());
                    }
                    EventDispatchStrategy strategy = strategies.get(entry.getStrategy().getName());
                    registry.register(entry.getEvent(), strategy);
                });
    }
}
