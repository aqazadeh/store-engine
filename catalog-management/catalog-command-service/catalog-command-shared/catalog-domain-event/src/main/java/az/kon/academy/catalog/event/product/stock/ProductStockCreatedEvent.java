package az.kon.academy.catalog.event.product.stock;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductStockCreatedEvent extends DomainEvent implements ProductStockEvent {

    private final UUID variantId;
    private final Integer quantity;

    public ProductStockCreatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    UUID variantId, Integer quantity) {
        super(eventId, aggregateId, timestamp, version);
        this.variantId = variantId;
        this.quantity = quantity;
    }

    private ProductStockCreatedEvent(String aggregateId, OffsetDateTime timestamp,
                                     UUID variantId, Integer quantity) {
        super(aggregateId, timestamp);
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public static ProductStockCreatedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              UUID variantId, Integer quantity) {
        return new ProductStockCreatedEvent(aggregateId, timestamp, variantId, quantity);
    }
}