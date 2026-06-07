package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductCategoryDeletedEvent extends DomainEvent implements ProductCategoryEvent {


    public ProductCategoryDeletedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                       Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    private ProductCategoryDeletedEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public static ProductCategoryDeletedEvent of(String aggregateId, OffsetDateTime timestamp) {
        return new ProductCategoryDeletedEvent(aggregateId, timestamp);
    }
}
