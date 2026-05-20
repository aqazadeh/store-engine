package az.kon.academy.catalog.event.product.stock;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductStockDecreasedEvent extends DomainEvent implements ProductStockEvent {

    private final Integer removedQuantity;
    private final Integer newQuantity;

    public ProductStockDecreasedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                      Integer removedQuantity, Integer newQuantity) {
        super(eventId, aggregateId, timestamp, version);
        this.removedQuantity = removedQuantity;
        this.newQuantity = newQuantity;
    }

    private ProductStockDecreasedEvent(String aggregateId, OffsetDateTime timestamp,
                                       Integer removedQuantity, Integer newQuantity) {
        super(aggregateId, timestamp);
        this.removedQuantity = removedQuantity;
        this.newQuantity = newQuantity;
    }

    public static ProductStockDecreasedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                Integer removedQuantity, Integer newQuantity) {
        return new ProductStockDecreasedEvent(aggregateId, timestamp, removedQuantity, newQuantity);
    }
}