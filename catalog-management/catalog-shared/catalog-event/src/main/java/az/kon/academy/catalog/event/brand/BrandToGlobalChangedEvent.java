package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

@Event(version = 1)
public final class BrandToGlobalChangedEvent extends DomainEvent implements BrandEvent{
    private final Boolean isGLobal;

    public BrandToGlobalChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, Boolean isGLobal) {
        super(eventId, aggregateId, timestamp, version);
        this.isGLobal = isGLobal;
    }

    private BrandToGlobalChangedEvent(String aggregateId, OffsetDateTime timestamp, Boolean isGLobal) {
        super(aggregateId, timestamp);
        this.isGLobal = isGLobal;
    }

    public static BrandToGlobalChangedEvent of(String aggregateId, OffsetDateTime timestamp, Boolean isGLobal) {
        return new BrandToGlobalChangedEvent(aggregateId, timestamp, isGLobal);
    }
}
