package az.kon.academy.catalog.event.productrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductRejectionReasonChangedReasonEvent extends DomainEvent implements ProductRejectionReasonEvent {

    private final String reason;
    private final UUID moderatedBy;

    public ProductRejectionReasonChangedReasonEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                                     Integer version, String reason, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    private ProductRejectionReasonChangedReasonEvent(String aggregateId, OffsetDateTime timestamp,
                                                      String reason, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    public static ProductRejectionReasonChangedReasonEvent of(String aggregateId, OffsetDateTime timestamp,
                                                               String reason, UUID moderatedBy) {
        return new ProductRejectionReasonChangedReasonEvent(aggregateId, timestamp, reason, moderatedBy);
    }
}
