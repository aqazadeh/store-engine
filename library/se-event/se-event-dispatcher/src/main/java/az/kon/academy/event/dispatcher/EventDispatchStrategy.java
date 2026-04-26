package az.kon.academy.event.dispatcher;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface EventDispatchStrategy {

    <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages);

    EventDispatchStrategy NOOP = new EventDispatchStrategy() {
        private static final Logger logger = LoggerFactory.getLogger(EventDispatchStrategy.class);

        @Override
        public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
            logger.warn("NoopEventDispatchStrategy: Received event of type {}, but no action is taken.",
                    eventMessages.stream().map(item -> item.getPayload().getClass().getSimpleName()).toList());
        }
    };
}
