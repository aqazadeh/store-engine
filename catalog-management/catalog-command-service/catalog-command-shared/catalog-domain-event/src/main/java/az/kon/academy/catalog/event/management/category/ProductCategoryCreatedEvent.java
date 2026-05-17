package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductCategoryCreatedEvent extends DomainEvent implements ProductCategoryEvent {
    private final String    name;
    private final String    path;
    private final String    description;
    private final String    image;

    public ProductCategoryCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                       String name, String path, String description, String image) {
        super(eventId, aggregateId, timestamp, version);
        this.name = name;
        this.path = path;
        this.description = description;
        this.image = image;
    }

    private ProductCategoryCreatedEvent(String aggregateId, OffsetDateTime timestamp, String name,
                                       String path, String description, String image) {
        super(aggregateId, timestamp);
        this.name = name;
        this.path = path;
        this.description = description;
        this.image = image;
    }

    public static ProductCategoryCreatedEvent of(String aggregateId, OffsetDateTime timestamp, String name,
                                                 String path, String description, String image) {
        return new ProductCategoryCreatedEvent(aggregateId, timestamp, name, path, description, image);
    }

}
