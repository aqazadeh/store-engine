package az.kon.academy.catalog.event.management.category;

public sealed interface ProductCategoryEvent permits
        ProductCategoryCreatedEvent, ProductCategoryImageChangedEvent, ProductCategoryInformationChangedEvent,
        ProductCategoryParentChangedEvent, ProductCategoryParentRemovedEvent,
        ProductCategoryActivatedEvent, ProductCategoryArchivedEvent, ProductCategoryDeletedEvent {
}
