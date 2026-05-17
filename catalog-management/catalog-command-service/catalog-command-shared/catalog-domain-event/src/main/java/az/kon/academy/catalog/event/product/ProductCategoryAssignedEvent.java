package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductCategoryAssignedEvent extends DomainEvent implements ProductEvent {

    private final UUID categoryId;

    public ProductCategoryAssignedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                        UUID categoryId) {
        super(eventId, aggregateId, timestamp, version);
        this.categoryId = categoryId;
    }

    private ProductCategoryAssignedEvent(String aggregateId, OffsetDateTime timestamp, UUID categoryId) {
        super(aggregateId, timestamp);
        this.categoryId = categoryId;
    }

    public static ProductCategoryAssignedEvent of(String aggregateId, OffsetDateTime timestamp, UUID categoryId) {
        return new ProductCategoryAssignedEvent(aggregateId, timestamp, categoryId);
    }
}