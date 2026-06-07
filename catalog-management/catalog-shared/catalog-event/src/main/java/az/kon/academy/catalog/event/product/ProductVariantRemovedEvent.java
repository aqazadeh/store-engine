package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantRemovedEvent extends DomainEvent implements ProductEvent {

    private final UUID variantId;

    public ProductVariantRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                      UUID variantId) {
        super(eventId, aggregateId, timestamp, version);
        this.variantId = variantId;
    }

    private ProductVariantRemovedEvent(String aggregateId, OffsetDateTime timestamp, UUID variantId) {
        super(aggregateId, timestamp);
        this.variantId = variantId;
    }

    public static ProductVariantRemovedEvent of(String aggregateId, OffsetDateTime timestamp, UUID variantId) {
        return new ProductVariantRemovedEvent(aggregateId, timestamp, variantId);
    }
}