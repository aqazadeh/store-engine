package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.dispatcher.exception.EventDispatchRegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventDispatchStrategyRegistry {

    private static final Logger logger = LoggerFactory.getLogger(EventDispatchStrategyRegistry.class);

    private final Map<Class<? extends AbstractEvent>, EventDispatchStrategy> strategies = new ConcurrentHashMap<>();

    private final ClassValue<Optional<EventDispatchStrategy>> strategyResolver = new ClassValue<>() {
        @Override
        protected Optional<EventDispatchStrategy> computeValue(Class<?> eventType) {
            EventDispatchStrategy direct = strategies.get(eventType);
            if (direct != null) return Optional.of(direct);
            return strategies.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(eventType))
                    .map(Map.Entry::getValue)
                    .findFirst();
        }
    };

    public void register(Class<? extends AbstractEvent> eventType, EventDispatchStrategy strategy) {
        register(eventType, strategy, false);
    }

    public void register(Class<? extends AbstractEvent> eventType, EventDispatchStrategy strategy, boolean overwrite) {

        if (Objects.isNull(eventType))
            throw new EventDispatchRegistryException("Event type cannot be null");
        if (Objects.isNull(strategy))
            throw new EventDispatchRegistryException("Strategy cannot be null");

        logger.info("Registering strategy | eventType={} strategy={}", eventType.getName(), strategy.getClass().getName());

        if (overwrite) {
            strategies.put(eventType, strategy);
        } else {
            EventDispatchStrategy existing = strategies.putIfAbsent(eventType, strategy);
            if (existing != null) {
                logger.error("Strategy already registered | eventType={}", eventType.getName());
                throw new EventDispatchRegistryException("Strategy already registered for event type: " + eventType.getName());
            }
        }
    }

    public Optional<EventDispatchStrategy> get(Class<? extends AbstractEvent> eventType) {
        logger.debug("Getting strategy | eventType={}", eventType.getName());
        return strategyResolver.get(eventType);
    }

}
