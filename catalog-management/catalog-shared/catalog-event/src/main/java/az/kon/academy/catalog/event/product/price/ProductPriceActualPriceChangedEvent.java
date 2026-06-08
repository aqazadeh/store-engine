package az.kon.academy.catalog.event.product.price;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductPriceActualPriceChangedEvent extends DomainEvent implements ProductPriceEvent {

    private final BigDecimal actualPrice;

    public ProductPriceActualPriceChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                               Integer version, BigDecimal actualPrice) {
        super(eventId, aggregateId, timestamp, version);
        this.actualPrice = actualPrice;
    }

    private ProductPriceActualPriceChangedEvent(String aggregateId, OffsetDateTime timestamp,
                                                 BigDecimal actualPrice) {
        super(aggregateId, timestamp);
        this.actualPrice = actualPrice;
    }

    public static ProductPriceActualPriceChangedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                          BigDecimal actualPrice) {
        return new ProductPriceActualPriceChangedEvent(aggregateId, timestamp, actualPrice);
    }
}
