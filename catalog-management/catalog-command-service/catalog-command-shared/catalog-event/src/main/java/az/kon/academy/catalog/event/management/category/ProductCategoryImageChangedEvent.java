package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductCategoryImageChangedEvent extends DomainEvent implements ProductCategoryEvent {

    private final String    image;

    public ProductCategoryImageChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                            Integer version,String image) {
        super(eventId, aggregateId, timestamp, version);
        this.image = image;
    }

    private ProductCategoryImageChangedEvent(String aggregateId, OffsetDateTime timestamp, String image) {
        super(aggregateId, timestamp);
        this.image = image;
    }

    public static ProductCategoryImageChangedEvent of(String aggregateId, OffsetDateTime timestamp, String image) {
        return new ProductCategoryImageChangedEvent(aggregateId, timestamp, image);
    }
}
