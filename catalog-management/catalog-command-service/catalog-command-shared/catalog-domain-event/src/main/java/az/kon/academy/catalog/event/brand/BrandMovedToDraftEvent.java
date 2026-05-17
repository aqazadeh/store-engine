package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public final class BrandMovedToDraftEvent extends DomainEvent implements BrandEvent {

    private final String status;

    public BrandMovedToDraftEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }


    private BrandMovedToDraftEvent(String aggregateId, OffsetDateTime timestamp, String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static BrandMovedToDraftEvent of(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status){
        return new BrandMovedToDraftEvent(eventId, aggregateId, timestamp, version, status);
    }

    public static BrandMovedToDraftEvent of(String aggregateId, OffsetDateTime timestamp, String status){
        return new BrandMovedToDraftEvent(aggregateId, timestamp, status);
    }
}
