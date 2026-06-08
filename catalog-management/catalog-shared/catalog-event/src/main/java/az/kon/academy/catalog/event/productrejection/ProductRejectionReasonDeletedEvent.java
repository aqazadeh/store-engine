package az.kon.academy.catalog.event.productrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductRejectionReasonDeletedEvent extends DomainEvent implements ProductRejectionReasonEvent {

    private final UUID moderatedBy;

    public ProductRejectionReasonDeletedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                               Integer version, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.moderatedBy = moderatedBy;
    }

    private ProductRejectionReasonDeletedEvent(String aggregateId, OffsetDateTime timestamp, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.moderatedBy = moderatedBy;
    }

    public static ProductRejectionReasonDeletedEvent of(String aggregateId, OffsetDateTime timestamp, UUID moderatedBy) {
        return new ProductRejectionReasonDeletedEvent(aggregateId, timestamp, moderatedBy);
    }
}
