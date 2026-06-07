package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductCategoryArchivedEvent extends DomainEvent implements ProductCategoryEvent {


    public ProductCategoryArchivedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                        Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    private ProductCategoryArchivedEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public static ProductCategoryArchivedEvent of(String aggregateId, OffsetDateTime timestamp) {
        return new ProductCategoryArchivedEvent(aggregateId, timestamp);
    }
}
