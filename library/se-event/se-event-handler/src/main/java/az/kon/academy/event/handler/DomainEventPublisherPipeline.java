package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.exception.EventHandlerNotFoundException;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class DomainEventPublisherPipeline implements DomainEventPublisher, EventHandlerInterceptorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisherPipeline.class);

    private static final List<EventHandlerInterceptor> interceptors = new CopyOnWriteArrayList<>();
    private final EventHandlerRegistry domainEventHandlerRegistry;

    public DomainEventPublisherPipeline(EventHandlerRegistry domainEventHandlerRegistry) {
        this.domainEventHandlerRegistry = domainEventHandlerRegistry;
    }


    @Override
    @SuppressWarnings({"unchecked"})
    public <E extends AbstractEvent> void publish(E event) {
        if (Objects.isNull(event)) return;
        try {
            interceptors.forEach(interceptor -> this.notifyInterceptor(event, interceptor::onReceive));
            var handler = this.domainEventHandlerRegistry.getHandlerForEvent(event.getClass());
            if (handler.isEmpty()) {
                throw new EventHandlerNotFoundException("No handler found for event type: " + event.getClass());
            }
            handler.get().handle(event);
        } catch (Exception e) {
            interceptors.forEach(interceptor -> interceptor.onError(event, e));
            throw e;
        } finally {
            interceptors.forEach(interceptor -> interceptor.onComplete(event));
        }
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
