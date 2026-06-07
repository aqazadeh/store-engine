package az.kon.academy.catalog.event.management.variant;

public sealed interface VariantValueEvent permits
        VariantValueCreatedEvent,
        VariantValueNameChangedEvent {
}