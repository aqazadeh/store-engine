package az.kon.academy.catalog.event.brand;

import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public final class BrandSentToApprovalEvent extends DomainEvent implements BrandEvent {

    private final String status;

    public BrandSentToApprovalEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }


    private BrandSentToApprovalEvent(String aggregateId, OffsetDateTime timestamp, String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static BrandSentToApprovalEvent of(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version, String status){
        return new BrandSentToApprovalEvent(eventId, aggregateId, timestamp, version, status);
    }

    public static BrandSentToApprovalEvent of(String aggregateId, OffsetDateTime timestamp, String status){
        return new BrandSentToApprovalEvent(aggregateId, timestamp, status);
    }
}
