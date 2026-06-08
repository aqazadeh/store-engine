package az.kon.academy.catalog.event.brandrejection;

public sealed interface BrandRejectionReasonEvent permits
        BrandRejectionReasonAddedEvent,
        BrandRejectionReasonChangedReasonEvent,
        BrandRejectionReasonDeletedEvent,
        BrandRejectionReasonSolvedEvent {
}