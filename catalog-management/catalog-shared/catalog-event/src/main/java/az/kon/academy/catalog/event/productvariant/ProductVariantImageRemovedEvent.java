package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantImageRemovedEvent extends DomainEvent implements ProductVariantEvent {

    private final String image;

    public ProductVariantImageRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                           String image
    ) {
        super(eventId, aggregateId, timestamp, version);
        this.image = image;
    }

    private ProductVariantImageRemovedEvent(String aggregateId, OffsetDateTime timestamp,
                                            String image
    ) {
        super(aggregateId, timestamp);
        this.image = image;
    }

    public static ProductVariantImageRemovedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                         String image
    ) {
        return new ProductVariantImageRemovedEvent(
                aggregateId, timestamp,
                image
        );
    }
}
