package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantRemovedEvent extends DomainEvent implements ProductVariantEvent {

    public ProductVariantRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version) {
        super(eventId, aggregateId, timestamp, version);
    }

    private ProductVariantRemovedEvent(String aggregateId, OffsetDateTime timestamp) {
        super(aggregateId, timestamp);
    }

    public static ProductVariantRemovedEvent create(String aggregateId, OffsetDateTime timestamp) {
        return new ProductVariantRemovedEvent(aggregateId, timestamp);
    }
}
