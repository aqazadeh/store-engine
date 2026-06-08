package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantImageAddedEvent extends DomainEvent implements ProductVariantEvent {

    private final String image;

    public ProductVariantImageAddedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                         String image
    ) {
        super(eventId, aggregateId, timestamp, version);
        this.image = image;
    }

    private ProductVariantImageAddedEvent(String aggregateId, OffsetDateTime timestamp,
                                          String image
    ) {
        super(aggregateId, timestamp);
        this.image = image;
    }

    public static ProductVariantImageAddedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                       String image
    ) {
        return new ProductVariantImageAddedEvent(
                aggregateId, timestamp,
                image
        );
    }
}
