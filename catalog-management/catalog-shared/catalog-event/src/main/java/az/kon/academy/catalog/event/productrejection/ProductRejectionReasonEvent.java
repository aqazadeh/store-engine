package az.kon.academy.catalog.event.productrejection;

public sealed interface ProductRejectionReasonEvent permits
        ProductRejectionReasonCreatedEvent, ProductRejectionReasonSolvedEvent,
        ProductRejectionReasonChangedReasonEvent, ProductRejectionReasonDeletedEvent {
}