package az.kon.academy.catalog.event.productrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
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
}
