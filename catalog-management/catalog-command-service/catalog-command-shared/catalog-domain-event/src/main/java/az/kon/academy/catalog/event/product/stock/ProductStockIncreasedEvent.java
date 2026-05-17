package az.kon.academy.catalog.event.product.stock;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductStockIncreasedEvent extends DomainEvent implements ProductStockEvent {

    private final Integer addedQuantity;
    private final Integer newQuantity;

    public ProductStockIncreasedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                      Integer addedQuantity, Integer newQuantity) {
        super(eventId, aggregateId, timestamp, version);
        this.addedQuantity = addedQuantity;
        this.newQuantity = newQuantity;
    }

    private ProductStockIncreasedEvent(String aggregateId, OffsetDateTime timestamp,
                                       Integer addedQuantity, Integer newQuantity) {
        super(aggregateId, timestamp);
        this.addedQuantity = addedQuantity;
        this.newQuantity = newQuantity;
    }

    public static ProductStockIncreasedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                Integer addedQuantity, Integer newQuantity) {
        return new ProductStockIncreasedEvent(aggregateId, timestamp, addedQuantity, newQuantity);
    }
}