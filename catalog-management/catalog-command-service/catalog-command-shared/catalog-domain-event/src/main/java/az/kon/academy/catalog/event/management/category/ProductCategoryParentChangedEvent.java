package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductCategoryParentChangedEvent extends DomainEvent implements ProductCategoryEvent {

    private final UUID parentId;

    public ProductCategoryParentChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                             Integer version, UUID parentId) {
        super(eventId, aggregateId, timestamp, version);
        this.parentId = parentId;
    }

    private ProductCategoryParentChangedEvent(String aggregateId, OffsetDateTime timestamp, UUID parentId) {
        super(aggregateId, timestamp);
        this.parentId = parentId;
    }

    public static ProductCategoryParentChangedEvent of(String aggregateId, OffsetDateTime timestamp, UUID parentId) {
        return new ProductCategoryParentChangedEvent(aggregateId, timestamp, parentId);
    }
}
