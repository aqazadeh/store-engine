package az.kon.academy.catalog.event.management.variant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class VariantKeyDescriptionChangedEvent extends DomainEvent implements VariantKeyEvent {

    private final String description;

    public VariantKeyDescriptionChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                             Integer version, String description) {
        super(eventId, aggregateId, timestamp, version);
        this.description = description;
    }

    private VariantKeyDescriptionChangedEvent(String aggregateId, OffsetDateTime timestamp, String description) {
        super(aggregateId, timestamp);
        this.description = description;
    }

    public static VariantKeyDescriptionChangedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                       String description) {
        return new VariantKeyDescriptionChangedEvent(aggregateId, timestamp, description);
    }
}