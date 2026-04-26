package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.dispatcher.exception.EventDispatchRegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatchStrategyRegistry {

    Logger logger = LoggerFactory.getLogger(EventDispatchStrategyRegistry.class);

    private final Map<Class<? extends AbstractEvent>, EventDispatchStrategy> strategies = new ConcurrentHashMap<>();

    private final ClassValue<EventDispatchStrategy> strategyResolver = new ClassValue<>() {
        @Override
        protected EventDispatchStrategy computeValue(Class<?> eventType) {
            return strategies.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(eventType))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
        }
    };

    public void register(Class<? extends AbstractEvent> eventType, EventDispatchStrategy strategy) {
        this.register(eventType, strategy, Boolean.FALSE);
    }

    public void register(Class<? extends AbstractEvent> eventType, EventDispatchStrategy strategy, Boolean overwrite) {

        if (Objects.isNull(eventType))
            throw new EventDispatchRegistryException("Event type cannot be null");
        if (Objects.isNull(strategy))
            throw new EventDispatchRegistryException("Strategy cannot be null");

        logger.info("Registering strategy for event type: {} with strategy: {}", eventType.getName(), strategy.getClass().getName());

        EventDispatchStrategy existing = strategies.putIfAbsent(eventType, strategy);

        if (!Objects.isNull(existing) && !overwrite) {
            logger.error("Strategy already registered for event type: {}", eventType.getName());
            throw new EventDispatchRegistryException("Strategy already registered for event type: " + eventType.getName());
        }
    }

    public Optional<EventDispatchStrategy> get(Class<? extends AbstractEvent> eventType) {
        logger.debug("Getting strategy for event type: {}", eventType.getName());
        return Optional.ofNullable(strategyResolver.get(eventType));
    }

    public List<Class<? extends AbstractEvent>> getRegisteredEventTypes() {
        return new ArrayList<>(strategies.keySet());
    }

}
