package az.kon.academy.catalog.event.management.rejection;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class BrandRejectionReasonCreatedEvent extends DomainEvent implements BrandRejectionReasonEvent {

    private final UUID brandId;
    private final String reason;
    private final UUID moderatedBy;

    public BrandRejectionReasonCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                            Integer version, UUID brandId, String reason, UUID moderatedBy) {
        super(eventId, aggregateId, timestamp, version);
        this.brandId = brandId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    private BrandRejectionReasonCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                             UUID brandId, String reason, UUID moderatedBy) {
        super(aggregateId, timestamp);
        this.brandId = brandId;
        this.reason = reason;
        this.moderatedBy = moderatedBy;
    }

    public static BrandRejectionReasonCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                      UUID brandId, String reason, UUID moderatedBy) {
        return new BrandRejectionReasonCreatedEvent(aggregateId, timestamp, brandId, reason, moderatedBy);
    }

    public UUID getBrandId() { return brandId; }
    public String getReason() { return reason; }
    public UUID getModeratedBy() { return moderatedBy; }
}