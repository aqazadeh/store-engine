package az.kon.academy.catalog.event.product;

public sealed interface ProductEvent permits
        ProductCreatedEvent,
        ProductSentToApprovalEvent, ProductApprovedEvent, ProductRejectedEvent,
        ProductMovedToDraftEvent, ProductArchivedEvent,
        ProductInformationChangedEvent,
        ProductCategoryAssignedEvent, ProductBrandAssignedEvent,
        ProductSpecificationAssignedEvent, ProductSpecificationRemovedEvent,
        ProductVariantAddedEvent, ProductVariantRemovedEvent {
}