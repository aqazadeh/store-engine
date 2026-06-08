package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductCreatedEvent extends DomainEvent implements ProductEvent {

    private final UUID merchantId;
    private final UUID categoryId;
    private final UUID brandId;
    private final String name;
    private final String description;
    private final String status;

    public ProductCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                               UUID merchantId,
                               UUID categoryId,
                               UUID brandId,
                               String name,
                               String description,
                               String status) {
        super(eventId, aggregateId, timestamp, version);
        this.merchantId = merchantId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    private ProductCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                UUID merchantId,
                                UUID categoryId,
                                UUID brandId,
                                String name,
                                String description,
                                String status) {
        super(aggregateId, timestamp);
        this.merchantId = merchantId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public static ProductCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                         UUID merchantId,
                                         UUID categoryId,
                                         UUID brandId,
                                         String name,
                                         String description,
                                         String status) {
        return new ProductCreatedEvent(
                aggregateId, timestamp,
                merchantId,
                categoryId,
                brandId,
                name,
                description,
                status
        );
    }
}