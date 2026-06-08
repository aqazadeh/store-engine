package az.kon.academy.catalog.event.management.specification;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSpecificationCreatedEvent extends DomainEvent implements ProductSpecificationEvent {

    private final String name;
    private final String description;

    public ProductSpecificationCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                            Integer version, String name, String description) {
        super(eventId, aggregateId, timestamp, version);
        this.name = name;
        this.description = description;
    }

    private ProductSpecificationCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                             String name, String description) {
        super(aggregateId, timestamp);
        this.name = name;
        this.description = description;
    }

    public static ProductSpecificationCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                      String name, String description) {
        return new ProductSpecificationCreatedEvent(aggregateId, timestamp, name, description);
    }
}
