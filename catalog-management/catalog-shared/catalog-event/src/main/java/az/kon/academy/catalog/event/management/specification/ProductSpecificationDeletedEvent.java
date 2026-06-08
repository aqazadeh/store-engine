package az.kon.academy.catalog.event.management.specification;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSpecificationDeletedEvent extends DomainEvent implements ProductSpecificationEvent {

    public ProductSpecificationDeletedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    private ProductSpecificationDeletedEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public static ProductSpecificationDeletedEvent of(String aggregateId, OffsetDateTime timestamp) {
        return new ProductSpecificationDeletedEvent(aggregateId, timestamp);
    }
}
