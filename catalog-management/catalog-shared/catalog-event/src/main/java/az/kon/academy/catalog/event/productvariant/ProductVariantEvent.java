package az.kon.academy.catalog.event.productvariant;

public sealed interface ProductVariantEvent permits
        ProductVariantActivatedEvent,
        ProductVariantAddedEvent,
        ProductVariantArchivedEvent,
        ProductVariantBarcodeChangedEvent,
        ProductVariantDeactivatedEvent,
        ProductVariantDiscontinuedEvent,
        ProductVariantImageAddedEvent,
        ProductVariantImageMarkedAsPrimaryEvent,
        ProductVariantImageRemovedEvent,
        ProductVariantMarkedOutOfStockEvent,
        ProductVariantRemovedEvent,
        ProductVariantSkuChangedEvent {
}
