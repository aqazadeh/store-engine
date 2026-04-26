package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.exception.DuplicateEventHandlerException;
import az.kon.academy.event.handler.exception.EventHandlerException;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class DomainEventPublisherPipeline implements DomainEventPublisher, EventHandlerRegistry, EventHandlerInterceptorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisherPipeline.class);

    private static final List<EventHandlerInterceptor> interceptors = new CopyOnWriteArrayList<>();
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <E extends AbstractEvent> void publish(E event) {
        if (Objects.isNull(event)) return;
        try {
            interceptors.forEach(interceptor -> this.notifyInterceptor(event, interceptor::onReceive));
            BaseEventHandler handler = handlerResolver.get(event.getClass());
            if (Objects.isNull(handler)) {
                throw new EventHandlerException("No handler found for event type: " + event.getClass());
            }
            handler.handle(event);
        } catch (Exception e) {
            interceptors.forEach(interceptor -> interceptor.onError(event, e));
            throw e;
        } finally {
            interceptors.forEach(interceptor -> interceptor.onComplete(event));
        }
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

    @Override
    public void registerInterceptor(EventHandlerInterceptor interceptor) {
        interceptors.add(interceptor);
        logger.info("Registered event handler interceptor: {}", interceptor.getClass());
    }

    private void notifyInterceptor(AbstractEvent event, Consumer<AbstractEvent> interceptor) {
        try {
            interceptor.accept(event);
        } catch (Exception e) {
            logger.error("Error in interceptor: ", e);
        }
    }
}
