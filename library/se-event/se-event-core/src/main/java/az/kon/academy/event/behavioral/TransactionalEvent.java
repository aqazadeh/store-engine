package az.kon.academy.event.behavioral;

import az.kon.academy.event.AbstractEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public class TransactionalEvent extends AbstractEvent {
    protected TransactionalEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public TransactionalEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
        super(eventId, aggregateId, timestamp);
    }

    public static  TransactionalEvent of(String aggregateId, OffsetDateTime timestamp) {
        return new TransactionalEvent(aggregateId, timestamp);
    }
}
