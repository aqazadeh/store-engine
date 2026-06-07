package az.kon.academy.catalog.event.brandrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandRejectionReasonChangedReasonEvent extends DomainEvent implements BrandRejectionReasonEvent {

    private final String reason;
    private final UUID moderatedBy;

    public BrandRejectionReasonChangedReasonEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                                  Integer version, String reason, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    private BrandRejectionReasonChangedReasonEvent(String aggregateId, OffsetDateTime timestamp,
                                                   String reason, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    public static BrandRejectionReasonChangedReasonEvent of(String aggregateId, OffsetDateTime timestamp,
                                                            String reason, UUID moderatedBy) {
        return new BrandRejectionReasonChangedReasonEvent(aggregateId, timestamp, reason, moderatedBy);
    }
}