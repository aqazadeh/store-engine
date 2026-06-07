package az.kon.academy.catalog.event.management.variant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class VariantValueNameChangedEvent extends DomainEvent implements VariantValueEvent {

    private final String name;

    public VariantValueNameChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                        Integer version, String name) {
        super(eventId, aggregateId, timestamp, version);
        this.name = name;
    }

    private VariantValueNameChangedEvent(String aggregateId, OffsetDateTime timestamp, String name) {
        super(aggregateId, timestamp);
        this.name = name;
    }

    public static VariantValueNameChangedEvent of(String aggregateId, OffsetDateTime timestamp, String name) {
        return new VariantValueNameChangedEvent(aggregateId, timestamp, name);
    }
}