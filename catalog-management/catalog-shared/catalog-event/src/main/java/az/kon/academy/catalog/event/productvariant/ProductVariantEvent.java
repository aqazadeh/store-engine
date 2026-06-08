package az.kon.academy.catalog.event.productvariant;

public sealed interface ProductVariantEvent permits
        ProductVariantAddedEvent,
        ProductVariantBarcodeChangedEvent,
        ProductVariantImageAddedEvent,
        ProductVariantImageMarkedAsPrimaryEvent,
        ProductVariantImageRemovedEvent {
}
