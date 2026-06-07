package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandOwnerChangedEvent extends DomainEvent implements BrandEvent {

    private final UUID owner;

    public BrandOwnerChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, UUID owner) {
        super(eventId, aggregateId, timestamp, version);
        this.owner = owner;
    }


    private BrandOwnerChangedEvent(String aggregateId, OffsetDateTime timestamp, UUID owner) {
        super(aggregateId, timestamp);
        this.owner = owner;
    }

    public static BrandOwnerChangedEvent of(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, UUID owner){
        return new BrandOwnerChangedEvent(eventId, aggregateId, timestamp, version, owner);
    }

    public static BrandOwnerChangedEvent of(String aggregateId, OffsetDateTime timestamp, UUID owner){
        return new BrandOwnerChangedEvent(aggregateId, timestamp, owner);
    }
}
