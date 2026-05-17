package az.kon.academy.event.handler.autoconfiguration.interceptor;


import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.autoconfiguration.annotation.EvenHandlerInterceptor;
import az.kon.academy.event.handler.exception.EventHandlerNotFoundException;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import az.kon.academy.event.handler.metric.EventHandlerMonitor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;

@EvenHandlerInterceptor
@ConditionalOnBooleanProperty(prefix = "store-engine.event.handler.metrics", name = "enabled", matchIfMissing = true)
public class EventHandlerMetricInterceptor implements EventHandlerInterceptor {

    private final EventHandlerMonitor eventHandlerMonitor;

    public EventHandlerMetricInterceptor(EventHandlerMonitor eventHandlerMonitor) {
        this.eventHandlerMonitor = eventHandlerMonitor;
    }

    @Override
    public <T extends AbstractEvent> void onReceive(T event) {
        this.eventHandlerMonitor.reportReceived(event.getClass().getSimpleName());
    }

    @Override
    public <T extends AbstractEvent> void onComplete(T event) {
        this.eventHandlerMonitor.reportSuccess(event.getClass().getSimpleName());
    }

    @Override
    public <T extends AbstractEvent> void onError(T event, Exception e) {
        if (e instanceof EventHandlerNotFoundException) {
            this.eventHandlerMonitor.reportHandlerNotFound(event.getClass().getSimpleName());
        } else {
            this.eventHandlerMonitor.reportFailure(event.getClass().getSimpleName(), e);
        }
    }
}
