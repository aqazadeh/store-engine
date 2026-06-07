package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSpecificationRemovedEvent extends DomainEvent implements ProductEvent {

    private final UUID specificationId;

    public ProductSpecificationRemovedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                            Integer version, UUID specificationId) {
        super(eventId, aggregateId, timestamp, version);
        this.specificationId = specificationId;
    }

    private ProductSpecificationRemovedEvent(String aggregateId, OffsetDateTime timestamp, UUID specificationId) {
        super(aggregateId, timestamp);
        this.specificationId = specificationId;
    }

    public static ProductSpecificationRemovedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                      UUID specificationId) {
        return new ProductSpecificationRemovedEvent(aggregateId, timestamp, specificationId);
    }
}