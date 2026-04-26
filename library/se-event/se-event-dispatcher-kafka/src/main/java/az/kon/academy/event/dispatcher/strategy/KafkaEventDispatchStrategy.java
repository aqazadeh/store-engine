package az.kon.academy.event.dispatcher.strategy;

import az.kon.academy.event.AbstractEvent;
import az.kon.academy.event.EventMessage;
import az.kon.academy.event.dispatcher.EventDispatchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KafkaEventDispatchStrategy implements EventDispatchStrategy {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventDispatchStrategy.class);

    @Override
    public <T extends AbstractEvent> void proceed(List<EventMessage<T>> eventMessages) {
        logger.debug("Dispatching Kafka event: {}", eventMessages.stream().map(item -> item.getPayload().getClass().getSimpleName()).toList());
    }
}

