package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSpecificationAssignedEvent extends DomainEvent implements ProductEvent {

    private final UUID specificationId;
    private final String value;

    public ProductSpecificationAssignedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                             Integer version, UUID specificationId, String value) {
        super(eventId, aggregateId, timestamp, version);
        this.specificationId = specificationId;
        this.value = value;
    }

    private ProductSpecificationAssignedEvent(String aggregateId, OffsetDateTime timestamp,
                                              UUID specificationId, String value) {
        super(aggregateId, timestamp);
        this.specificationId = specificationId;
        this.value = value;
    }

    public static ProductSpecificationAssignedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                       UUID specificationId, String value) {
        return new ProductSpecificationAssignedEvent(aggregateId, timestamp, specificationId, value);
    }
}