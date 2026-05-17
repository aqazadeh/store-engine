package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface EventDispatchStrategy {

    <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages);

    EventDispatchStrategy NOOP = new NoopEventDispatchStrategy();

    class NoopEventDispatchStrategy implements EventDispatchStrategy {
        private static final Logger logger = LoggerFactory.getLogger(NoopEventDispatchStrategy.class);

        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
            logger.warn("No strategy registered for event types: {}",
                    eventMessages.stream().map(m -> m.getPayload().getClass().getSimpleName()).toList());
        }
    }
}
