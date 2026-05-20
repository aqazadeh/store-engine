package az.kon.academy.catalog.event.product.price;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductPriceCreatedEvent extends DomainEvent implements ProductPriceEvent {

    private final UUID variantId;
    private final String minPrice;
    private final String maxPrice;

    public ProductPriceCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    UUID variantId, String minPrice, String maxPrice) {
        super(eventId, aggregateId, timestamp, version);
        this.variantId = variantId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    private ProductPriceCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                     UUID variantId, String minPrice, String maxPrice) {
        super(aggregateId, timestamp);
        this.variantId = variantId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public static ProductPriceCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              UUID variantId, String minPrice, String maxPrice) {
        return new ProductPriceCreatedEvent(aggregateId, timestamp, variantId, minPrice, maxPrice);
    }
}