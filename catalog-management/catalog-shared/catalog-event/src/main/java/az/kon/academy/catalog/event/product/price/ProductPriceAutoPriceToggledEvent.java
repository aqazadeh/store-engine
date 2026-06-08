package az.kon.academy.catalog.event.product.price;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductPriceAutoPriceToggledEvent extends DomainEvent implements ProductPriceEvent {

    private final Boolean autoPriceChangeEnabled;

    public ProductPriceAutoPriceToggledEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                             Integer version, Boolean autoPriceChangeEnabled) {
        super(eventId, aggregateId, timestamp, version);
        this.autoPriceChangeEnabled = autoPriceChangeEnabled;
    }

    private ProductPriceAutoPriceToggledEvent(String aggregateId, OffsetDateTime timestamp,
                                               Boolean autoPriceChangeEnabled) {
        super(aggregateId, timestamp);
        this.autoPriceChangeEnabled = autoPriceChangeEnabled;
    }

    public static ProductPriceAutoPriceToggledEvent of(String aggregateId, OffsetDateTime timestamp,
                                                        Boolean autoPriceChangeEnabled) {
        return new ProductPriceAutoPriceToggledEvent(aggregateId, timestamp, autoPriceChangeEnabled);
    }
}
