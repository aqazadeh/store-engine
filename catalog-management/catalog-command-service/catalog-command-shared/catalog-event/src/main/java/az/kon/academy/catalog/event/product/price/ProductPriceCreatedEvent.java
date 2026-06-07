package az.kon.academy.catalog.event.product.price;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductPriceCreatedEvent extends DomainEvent implements ProductPriceEvent {

    private final UUID variantId;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final BigDecimal defaultPrice;
    private final BigDecimal actualPrice;
    private final Boolean autoPriceChangeEnabled;

    public ProductPriceCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    UUID variantId,
                                    BigDecimal minPrice,
                                    BigDecimal maxPrice,
                                    BigDecimal defaultPrice,
                                    BigDecimal actualPrice,
                                    Boolean autoPriceChangeEnabled) {
        super(eventId, aggregateId, timestamp, version);
        this.variantId = variantId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.defaultPrice = defaultPrice;
        this.actualPrice = actualPrice;
        this.autoPriceChangeEnabled = autoPriceChangeEnabled;
    }

    private ProductPriceCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                     UUID variantId,
                                     BigDecimal minPrice,
                                     BigDecimal maxPrice,
                                     BigDecimal defaultPrice,
                                     BigDecimal actualPrice,
                                     Boolean autoPriceChangeEnabled) {
        super(aggregateId, timestamp);
        this.variantId = variantId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.defaultPrice = defaultPrice;
        this.actualPrice = actualPrice;
        this.autoPriceChangeEnabled = autoPriceChangeEnabled;
    }

    public static ProductPriceCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              UUID variantId,
                                              BigDecimal minPrice,
                                              BigDecimal maxPrice,
                                              BigDecimal defaultPrice,
                                              BigDecimal actualPrice,
                                              Boolean autoPriceChangeEnabled) {
        return new ProductPriceCreatedEvent(
                aggregateId,
                timestamp,
                variantId,
                minPrice,
                maxPrice,
                defaultPrice,
                actualPrice,
                autoPriceChangeEnabled
        );
    }
}