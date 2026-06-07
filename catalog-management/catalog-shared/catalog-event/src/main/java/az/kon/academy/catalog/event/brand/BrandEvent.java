package az.kon.academy.catalog.event.brand;

public sealed interface BrandEvent permits
        BrandCreatedEvent, BrandCreatedGlobalEvent,
        BrandSentToApprovalEvent, BrandApprovedEvent, BrandRejectedEvent, BrandMovedToDraftEvent, BrandMovedToInReviewEvent,
        BrandInformationChangedEvent, BrandImageChangedEvent, BrandOwnerChangedEvent, BrandToGlobalChangedEvent {
}
