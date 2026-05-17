package az.kon.academy.catalog.event.management.specification;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductSpecificationCategoryAssignedEvent extends DomainEvent implements ProductSpecificationEvent {

    private final UUID    categoryId;
    private final boolean required;

    public ProductSpecificationCategoryAssignedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                                     Integer version, UUID categoryId, boolean required) {
        super(eventId, aggregateId, timestamp, version);
        this.categoryId = categoryId;
        this.required = required;
    }

    private ProductSpecificationCategoryAssignedEvent(String aggregateId, OffsetDateTime timestamp,
                                                      UUID categoryId, boolean required) {
        super(aggregateId, timestamp);
        this.categoryId = categoryId;
        this.required = required;
    }

    public static ProductSpecificationCategoryAssignedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                               UUID categoryId, boolean required) {
        return new ProductSpecificationCategoryAssignedEvent(aggregateId, timestamp, categoryId, required);
    }
}