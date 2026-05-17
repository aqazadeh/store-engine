package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.exception.DuplicateEventHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDomainEventRegistry implements EventHandlerRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDomainEventRegistry.class);

    private final Map<Class<? extends AbstractEvent>, BaseEventHandler<? extends AbstractEvent>> eventHandlers = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    private final ClassValue<BaseEventHandler> handlerResolver = new ClassValue<>() {
        @Override
        protected BaseEventHandler computeValue(Class<?> type) {
            Class<?> current = type;
            while (Objects.nonNull(current) && current != Object.class) {
                BaseEventHandler handler = eventHandlers.get(current);
                if (Objects.nonNull(handler)) {
                    return handler;
                }
                current = current.getSuperclass();
            }
            return null;
        }
    };

    @Override
    @SuppressWarnings("rawtypes")
    public Optional<BaseEventHandler<?>> getHandlerForEvent(Class<? extends AbstractEvent> eventType) {
        BaseEventHandler raw = this.handlerResolver.get(eventType);
        return Optional.ofNullable((BaseEventHandler<?>) raw);
    }

    @Override
    public void registerHandler(BaseEventHandler<? extends AbstractEvent> handler, Class<? extends AbstractEvent> eventType) {
        Objects.requireNonNull(handler, "handler must not be null");
        Objects.requireNonNull(eventType, "eventType must not be null");
        eventHandlers.compute(eventType, (k, existingHandler) -> {
            if (Objects.isNull(existingHandler)) {
                logger.debug("Registered handler for event: {}", handler.getClass().getSimpleName());
                return handler;
            }
            throw new DuplicateEventHandlerException(eventType + " is already registered for " + existingHandler);
        });
    }
}
