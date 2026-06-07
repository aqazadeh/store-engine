package az.kon.academy.catalog.event.brandrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandRejectionReasonDeletedEvent extends DomainEvent implements BrandRejectionReasonEvent {
    private final UUID moderatedBy;

    public BrandRejectionReasonDeletedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                            Integer version, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.moderatedBy = moderatedBy;
    }

    private BrandRejectionReasonDeletedEvent(String aggregateId, OffsetDateTime timestamp, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.moderatedBy = moderatedBy;
    }

    public static BrandRejectionReasonDeletedEvent of(String aggregateId, OffsetDateTime timestamp, UUID moderatedBy) {
        return new BrandRejectionReasonDeletedEvent(aggregateId, timestamp, moderatedBy);
    }
}