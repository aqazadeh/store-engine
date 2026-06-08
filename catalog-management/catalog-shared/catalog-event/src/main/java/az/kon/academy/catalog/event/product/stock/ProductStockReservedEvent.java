package az.kon.academy.catalog.event.product.stock;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductStockReservedEvent extends DomainEvent implements ProductStockEvent {

    private final Integer reservedQuantity;
    private final Integer newReservedQuantity;

    public ProductStockReservedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                     Integer reservedQuantity, Integer newReservedQuantity) {
        super(eventId, aggregateId, timestamp, version);
        this.reservedQuantity = reservedQuantity;
        this.newReservedQuantity = newReservedQuantity;
    }

    private ProductStockReservedEvent(String aggregateId, OffsetDateTime timestamp,
                                      Integer reservedQuantity, Integer newReservedQuantity) {
        super(aggregateId, timestamp);
        this.reservedQuantity = reservedQuantity;
        this.newReservedQuantity = newReservedQuantity;
    }

    public static ProductStockReservedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                Integer reservedQuantity, Integer newReservedQuantity) {
        return new ProductStockReservedEvent(aggregateId, timestamp, reservedQuantity, newReservedQuantity);
    }
}
