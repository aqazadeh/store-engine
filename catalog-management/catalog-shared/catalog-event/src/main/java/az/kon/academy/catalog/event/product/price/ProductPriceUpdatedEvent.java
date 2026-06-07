package az.kon.academy.catalog.event.product.price;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductPriceUpdatedEvent extends DomainEvent implements ProductPriceEvent {

    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    public ProductPriceUpdatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    BigDecimal minPrice,
                                    BigDecimal maxPrice) {
        super(eventId, aggregateId, timestamp, version);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    private ProductPriceUpdatedEvent(String aggregateId, OffsetDateTime timestamp,
                                     BigDecimal minPrice,
                                     BigDecimal maxPrice) {
        super(aggregateId, timestamp);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public static ProductPriceUpdatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              BigDecimal minPrice, BigDecimal maxPrice) {
        return new ProductPriceUpdatedEvent(
                aggregateId, timestamp,
                minPrice,
                maxPrice
        );
    }
}