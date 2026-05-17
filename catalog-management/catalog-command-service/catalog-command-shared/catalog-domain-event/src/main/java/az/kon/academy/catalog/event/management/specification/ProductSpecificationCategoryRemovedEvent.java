package az.kon.academy.catalog.event.management.specification;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductSpecificationCategoryRemovedEvent extends DomainEvent implements ProductSpecificationEvent {

    private final UUID categoryId;

    public ProductSpecificationCategoryRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                                    Integer version, UUID categoryId) {
        super(eventId, aggregateId, timestamp, version);
        this.categoryId = categoryId;
    }

    private ProductSpecificationCategoryRemovedEvent(String aggregateId, OffsetDateTime timestamp, UUID categoryId) {
        super(aggregateId, timestamp);
        this.categoryId = categoryId;
    }

    public static ProductSpecificationCategoryRemovedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                              UUID categoryId) {
        return new ProductSpecificationCategoryRemovedEvent(aggregateId, timestamp, categoryId);
    }
}