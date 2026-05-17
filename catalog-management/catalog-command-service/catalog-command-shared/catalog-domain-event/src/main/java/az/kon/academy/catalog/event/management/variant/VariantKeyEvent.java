package az.kon.academy.catalog.event.management.variant;

public sealed interface VariantKeyEvent permits
        VariantKeyCreatedEvent,
        VariantKeyNameChangedEvent,
        VariantKeyDescriptionChangedEvent {
}
