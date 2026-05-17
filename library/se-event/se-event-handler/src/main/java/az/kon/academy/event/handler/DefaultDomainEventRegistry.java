package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.exception.DuplicateEventHandlerException;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDomainEventRegistry implements EventHandlerRegistry{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultDomainEventRegistry.class);
    private static final Map<Class<? extends AbstractEvent>, BaseEventHandler<? extends AbstractEvent>> eventHandlers = new ConcurrentHashMap<>();

    @SuppressWarnings("rawtypes")
    private final ClassValue<BaseEventHandler> handlerResolver = new ClassValue<>() {
        @Override
        protected BaseEventHandler computeValue(Class<?> type) {

            if (Objects.isNull(type) || type == Object.class) {
                return null;
            }

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
    @SuppressWarnings({"rawtypes"})
    public Optional<BaseEventHandler> getHandlerForEvent(Class<? extends AbstractEvent> eventType) {
        BaseEventHandler handler = this.handlerResolver.get(eventType);
        return Optional.ofNullable(handler);
    }

    @Override
    public void registerHandler(BaseEventHandler<? extends AbstractEvent> handler, Class<? extends AbstractEvent> eventType) {
        eventHandlers.compute(eventType, (k, existingHandler) -> {
            if (Objects.isNull(existingHandler) || Objects.isNull(handler)) {
                logger.debug("Registered handler for command: {}", handler.getClass().getSimpleName());
                return handler;
            } else {
                throw new DuplicateEventHandlerException(eventType + " is already registered for " + handler);
            }
        });
    }
}
