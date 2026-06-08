package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantSkuChangedEvent extends DomainEvent implements ProductVariantEvent {

    private final String sku;

    public ProductVariantSkuChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                         String sku) {
        super(eventId, aggregateId, timestamp, version);
        this.sku = sku;
    }

    private ProductVariantSkuChangedEvent(String aggregateId, OffsetDateTime timestamp,
                                          String sku) {
        super(aggregateId, timestamp);
        this.sku = sku;
    }

    public static ProductVariantSkuChangedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                       String sku) {
        return new ProductVariantSkuChangedEvent(aggregateId, timestamp, sku);
    }
}
