package az.kon.academy.event.handler;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.exception.EventHandlerNotFoundException;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptorRegistry;
import az.kon.academy.event.handler.metric.EventHandlerMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DomainEventPublisherPipeline implements DomainEventPublisher, EventHandlerInterceptorRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DomainEventPublisherPipeline.class);

    private final List<EventHandlerInterceptor> interceptors = new CopyOnWriteArrayList<>();
    private final EventHandlerRegistry domainEventHandlerRegistry;
    private final EventHandlerMonitor monitor;

    public DomainEventPublisherPipeline(EventHandlerRegistry domainEventHandlerRegistry, EventHandlerMonitor monitor) {
        this.domainEventHandlerRegistry = domainEventHandlerRegistry;
        this.monitor = monitor;
    }

    public DomainEventPublisherPipeline(EventHandlerRegistry domainEventHandlerRegistry) {
        this(domainEventHandlerRegistry, EventHandlerMonitor.NOOP);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends AbstractEvent> void publish(E event) {
        if (Objects.isNull(event)) return;
        String eventName = event.getClass().getSimpleName();
        try {
            monitor.reportReceived(eventName);
            interceptors.forEach(interceptor -> notifyInterceptor(event, interceptor::onReceive));

            var handler = this.domainEventHandlerRegistry.getHandlerForEvent(event.getClass());
            if (handler.isEmpty()) {
                monitor.reportHandlerNotFound(eventName);
                throw new EventHandlerNotFoundException("No handler found for event type: " + event.getClass());
            }

            interceptors.forEach(interceptor -> notifyInterceptor(event, interceptor::preExecution));
            ((BaseEventHandler<E>) handler.get()).handle(event);
            interceptors.forEach(interceptor -> notifyInterceptor(event, interceptor::postExecution));
            monitor.reportSuccess(eventName);
        } catch (Exception e) {
            monitor.reportFailure(eventName, e);
            interceptors.forEach(interceptor -> notifyInterceptor(event, e, interceptor::onError));
            throw e;
        } finally {
            interceptors.forEach(interceptor -> notifyInterceptor(event, interceptor::onComplete));
        }
    }

    @Override
    public void registerInterceptor(EventHandlerInterceptor interceptor) {
        interceptors.add(interceptor);
        logger.info("Registered event handler interceptor: {}", interceptor.getClass());
    }

    private <E extends AbstractEvent> void notifyInterceptor(E event, Consumer<E> fn) {
        try {
            fn.accept(event);
        } catch (Exception e) {
            logger.error("Error in interceptor: ", e);
        }
    }

    private <E extends AbstractEvent> void notifyInterceptor(E event, Exception cause, BiConsumer<E, Exception> fn) {
        try {
            fn.accept(event, cause);
        } catch (Exception e) {
            logger.error("Error in interceptor onError: ", e);
        }
    }
}
