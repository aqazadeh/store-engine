package az.kon.academy.catalog.event.brand;


import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandImageChangedEvent extends DomainEvent implements BrandEvent {
    private final String image;

    public BrandImageChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String image) {
        super(eventId, aggregateId, timestamp, version);
        this.image = image;
    }

    private BrandImageChangedEvent(String aggregateId, OffsetDateTime timestamp, String image) {
        super(aggregateId, timestamp);
        this.image = image;
    }

    public static BrandImageChangedEvent of(String aggregateId, OffsetDateTime timestamp, String image){
        return new BrandImageChangedEvent(aggregateId, timestamp, image);
    }
}
