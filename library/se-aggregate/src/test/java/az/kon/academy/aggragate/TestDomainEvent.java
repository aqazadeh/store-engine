package az.kon.academy.aggragate;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;

@Event(version = 1)
public class TestDomainEvent extends DomainEvent {
    public TestDomainEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }
}
