package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public final class BrandApprovedEvent extends DomainEvent implements BrandEvent {

    private final String status;

    public BrandApprovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }


    private BrandApprovedEvent(String aggregateId, OffsetDateTime timestamp, String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static BrandApprovedEvent of(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status){
        return new BrandApprovedEvent(eventId, aggregateId, timestamp, version, status);
    }

    public static BrandApprovedEvent of(String aggregateId, OffsetDateTime timestamp, String status){
        return new BrandApprovedEvent(aggregateId, timestamp, status);
    }
}
