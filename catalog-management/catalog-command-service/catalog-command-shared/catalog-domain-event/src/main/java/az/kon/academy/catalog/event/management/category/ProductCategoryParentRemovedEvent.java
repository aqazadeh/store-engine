package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductCategoryParentRemovedEvent extends DomainEvent implements ProductCategoryEvent {

    public ProductCategoryParentRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    private ProductCategoryParentRemovedEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public static ProductCategoryParentRemovedEvent of(String aggregateId, OffsetDateTime timestamp) {
        return new ProductCategoryParentRemovedEvent(aggregateId, timestamp);
    }
}
