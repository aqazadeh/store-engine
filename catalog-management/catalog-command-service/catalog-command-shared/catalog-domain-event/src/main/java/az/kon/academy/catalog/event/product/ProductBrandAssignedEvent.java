package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductBrandAssignedEvent extends DomainEvent implements ProductEvent {

    private final UUID brandId;

    public ProductBrandAssignedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                     UUID brandId) {
        super(eventId, aggregateId, timestamp, version);
        this.brandId = brandId;
    }

    private ProductBrandAssignedEvent(String aggregateId, OffsetDateTime timestamp, UUID brandId) {
        super(aggregateId, timestamp);
        this.brandId = brandId;
    }

    public static ProductBrandAssignedEvent of(String aggregateId, OffsetDateTime timestamp, UUID brandId) {
        return new ProductBrandAssignedEvent(aggregateId, timestamp, brandId);
    }
}