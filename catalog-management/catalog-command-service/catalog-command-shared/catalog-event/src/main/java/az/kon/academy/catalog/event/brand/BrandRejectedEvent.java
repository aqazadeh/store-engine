package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandRejectedEvent extends DomainEvent implements BrandEvent {

    private final String status;

    public BrandRejectedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }


    private BrandRejectedEvent(String aggregateId, OffsetDateTime timestamp, String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static BrandRejectedEvent of(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status){
        return new BrandRejectedEvent(eventId, aggregateId, timestamp, version, status);
    }

    public static BrandRejectedEvent of(String aggregateId, OffsetDateTime timestamp, String status){
        return new BrandRejectedEvent(aggregateId, timestamp, status);
    }
}
