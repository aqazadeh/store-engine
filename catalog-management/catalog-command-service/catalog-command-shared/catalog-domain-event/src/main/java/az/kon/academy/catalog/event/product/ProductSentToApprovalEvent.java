package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSentToApprovalEvent extends DomainEvent implements ProductEvent {

    private final String status;

    public ProductSentToApprovalEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                      String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }

    private ProductSentToApprovalEvent(String aggregateId, OffsetDateTime timestamp, String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static ProductSentToApprovalEvent of(String aggregateId, OffsetDateTime timestamp, String status) {
        return new ProductSentToApprovalEvent(aggregateId, timestamp, status);
    }
}