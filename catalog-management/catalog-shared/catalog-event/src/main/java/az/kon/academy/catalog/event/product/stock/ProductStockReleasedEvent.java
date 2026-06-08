package az.kon.academy.catalog.event.product.stock;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductStockReleasedEvent extends DomainEvent implements ProductStockEvent {

    private final Integer releasedQuantity;
    private final Integer newReservedQuantity;

    public ProductStockReleasedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                     Integer releasedQuantity, Integer newReservedQuantity) {
        super(eventId, aggregateId, timestamp, version);
        this.releasedQuantity = releasedQuantity;
        this.newReservedQuantity = newReservedQuantity;
    }

    private ProductStockReleasedEvent(String aggregateId, OffsetDateTime timestamp,
                                      Integer releasedQuantity, Integer newReservedQuantity) {
        super(aggregateId, timestamp);
        this.releasedQuantity = releasedQuantity;
        this.newReservedQuantity = newReservedQuantity;
    }

    public static ProductStockReleasedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                Integer releasedQuantity, Integer newReservedQuantity) {
        return new ProductStockReleasedEvent(aggregateId, timestamp, releasedQuantity, newReservedQuantity);
    }
}
