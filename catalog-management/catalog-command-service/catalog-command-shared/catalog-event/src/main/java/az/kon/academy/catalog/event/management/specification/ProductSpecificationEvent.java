package az.kon.academy.catalog.event.management.specification;

public sealed interface ProductSpecificationEvent permits
        ProductSpecificationCreatedEvent,
        ProductSpecificationInformationChangedEvent,
        ProductSpecificationCategoryAssignedEvent,
        ProductSpecificationCategoryRemovedEvent {
}