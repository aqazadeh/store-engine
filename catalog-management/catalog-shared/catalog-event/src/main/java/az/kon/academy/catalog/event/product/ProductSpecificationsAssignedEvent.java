package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductSpecificationsAssignedEvent extends DomainEvent implements ProductEvent {

    private final List<UUID> specificationIds;
    private final List<String> values;

    public ProductSpecificationsAssignedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp,
                                               Integer version, List<UUID> specificationIds, List<String> values) {
        super(eventId, aggregateId, timestamp, version);
        this.specificationIds = specificationIds;
        this.values = values;
    }

    private ProductSpecificationsAssignedEvent(String aggregateId, OffsetDateTime timestamp,
                                                List<UUID> specificationIds, List<String> values) {
        super(aggregateId, timestamp);
        this.specificationIds = specificationIds;
        this.values = values;
    }

    public static ProductSpecificationsAssignedEvent of(String aggregateId, OffsetDateTime timestamp,
                                                         List<UUID> specificationIds, List<String> values) {
        return new ProductSpecificationsAssignedEvent(aggregateId, timestamp, specificationIds, values);
    }
}
