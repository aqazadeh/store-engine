package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantDeactivatedEvent extends DomainEvent implements ProductVariantEvent {

    private final String status;

    public ProductVariantDeactivatedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                          String status) {
        super(eventId, aggregateId, timestamp, version);
        this.status = status;
    }

    private ProductVariantDeactivatedEvent(String aggregateId, OffsetDateTime timestamp,
                                           String status) {
        super(aggregateId, timestamp);
        this.status = status;
    }

    public static ProductVariantDeactivatedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                        String status) {
        return new ProductVariantDeactivatedEvent(aggregateId, timestamp, status);
    }
}
