package az.kon.academy.catalog.event.management.rejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

@Event(version = 1)
public final class ProductRejectionReasonCreatedEvent extends DomainEvent implements ProductRejectionReasonEvent {

    private final UUID productId;
    private final String reason;
    private final UUID moderatedBy;

    public ProductRejectionReasonCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                              Integer version, UUID productId, String reason, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.productId = productId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    private ProductRejectionReasonCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                               UUID productId, String reason, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.productId = productId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    public static ProductRejectionReasonCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                        UUID productId, String reason, UUID moderatedBy) {
        return new ProductRejectionReasonCreatedEvent(aggregateId, timestamp, productId, reason, moderatedBy);
    }

    public UUID getProductId() { return productId; }
    public String getReason() { return reason; }
    public UUID getModeratedBy() { return moderatedBy; }
}
