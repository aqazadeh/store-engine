package az.kon.academy.catalog.event.management.variant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class VariantValueCreatedEvent extends DomainEvent implements VariantValueEvent {

    private final UUID   keyId;
    private final String name;

    public VariantValueCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                    Integer version, UUID keyId, String name) {
        super(eventId, aggregateId, timestamp, version);
        this.keyId = keyId;
        this.name = name;
    }

    private VariantValueCreatedEvent(String aggregateId, OffsetDateTime timestamp, UUID keyId, String name) {
        super(aggregateId, timestamp);
        this.keyId = keyId;
        this.name = name;
    }

    public static VariantValueCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              UUID keyId, String name) {
        return new VariantValueCreatedEvent(aggregateId, timestamp, keyId, name);
    }
}