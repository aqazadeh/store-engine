package az.kon.academy.catalog.event.brandrejection;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class BrandRejectionReasonAddedEvent extends DomainEvent implements BrandRejectionReasonEvent {

    private final UUID brandId;
    private final String reason;
    private final UUID moderatedBy;
    private final Boolean solved;

    public BrandRejectionReasonAddedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                          Integer version, UUID brandId, String reason, UUID moderatedBy, Boolean solved) {
        super(eventId, aggregateId, timestamp, version);
        this.brandId = brandId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
        this.solved = solved;
    }

    private BrandRejectionReasonAddedEvent(String aggregateId, OffsetDateTime timestamp,
                                           UUID brandId, String reason, UUID moderatedBy, Boolean solved) {
        super(aggregateId, timestamp);
        this.brandId = brandId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
        this.solved = solved;
    }

    public static BrandRejectionReasonAddedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                    UUID brandId, String reason, UUID moderatedBy, Boolean solved) {
        return new BrandRejectionReasonAddedEvent(aggregateId, timestamp, brandId, reason, moderatedBy, solved);
    }
}