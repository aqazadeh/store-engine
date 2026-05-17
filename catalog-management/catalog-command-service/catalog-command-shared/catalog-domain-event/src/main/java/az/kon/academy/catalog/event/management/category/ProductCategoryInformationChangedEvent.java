package az.kon.academy.catalog.event.management.category;

import az.kon.academy.event.behavioral.DomainEvent;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class ProductCategoryInformationChangedEvent extends DomainEvent implements ProductCategoryEvent {
    private final String    name;
    private final String    path;
    private final String    description;

    public ProductCategoryInformationChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                                  Integer version, String name, String path, String description) {
        super(eventId, aggregateId, timestamp, version);
        this.name = name;
        this.path = path;
        this.description = description;
    }

    private ProductCategoryInformationChangedEvent(String aggregateId, OffsetDateTime timestamp, String name,
                                                   String path, String description) {
        super(aggregateId, timestamp);
        this.name = name;
        this.path = path;
        this.description = description;
    }

    public static ProductCategoryInformationChangedEvent of(String aggregateId, OffsetDateTime timestamp,String name,
                                                            String path, String description) {
        return new ProductCategoryInformationChangedEvent(aggregateId, timestamp, name, path, description);
    }

}
