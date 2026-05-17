package az.kon.academy.event.handler.autoconfiguration.interceptor;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.handler.autoconfiguration.annotation.EvenHandlerInterceptor;
import az.kon.academy.event.handler.interceptor.EventHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;


@EvenHandlerInterceptor
@ConditionalOnBooleanProperty(prefix = "store-engine.event.handler.logging", name = "enabled", matchIfMissing = true)
public class EventHandlerLoggingInterceptor implements EventHandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(EventHandlerLoggingInterceptor.class);

    @Override
    public <T extends AbstractEvent> void onReceive(T event) {
        logger.info("Received event: {}", event.getClass().getSimpleName());
    }

    @Override
    public <T extends AbstractEvent> void onComplete(T event) {
        logger.info("Completed processing event: {}", event.getClass().getSimpleName());
    }

    @Override
    public <T extends AbstractEvent> void onError(T event, Exception e) {
        logger.error("Error processing event: {}", event.getClass().getSimpleName(), e);
    }
}
