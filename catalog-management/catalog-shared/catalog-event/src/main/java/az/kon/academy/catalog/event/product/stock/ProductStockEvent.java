package az.kon.academy.catalog.event.product.stock;

public sealed interface ProductStockEvent permits
        ProductStockCreatedEvent, ProductStockDecreasedEvent, ProductStockIncreasedEvent,
        ProductStockReservedEvent, ProductStockReleasedEvent {
}