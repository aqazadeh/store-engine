package az.kon.academy.event.behavioral;

import az.kon.academy.event.AbstractEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class DomainEvent  extends AbstractEvent {

    public DomainEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    protected DomainEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    protected DomainEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp) {
        super(eventId, aggregateId, timestamp);
    }
}
