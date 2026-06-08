package az.kon.academy.catalog.event.product.price;

public sealed interface ProductPriceEvent permits
        ProductPriceCreatedEvent, ProductPriceUpdatedEvent,
        ProductPriceActualPriceChangedEvent, ProductPriceAutoPriceToggledEvent {
}