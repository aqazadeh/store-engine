package az.kon.academy.catalog.event.product;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantAddedEvent extends DomainEvent implements ProductEvent {

    private final UUID variantId;
    private final List<UUID> variantKeyIds;
    private final List<UUID> variantValueIds;

    public ProductVariantAddedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    UUID variantId, List<UUID> variantKeyIds, List<UUID> variantValueIds) {
        super(eventId, aggregateId, timestamp, version);
        this.variantId = variantId;
        this.variantKeyIds = variantKeyIds;
        this.variantValueIds = variantValueIds;
    }

    private ProductVariantAddedEvent(String aggregateId, OffsetDateTime timestamp,
                                     UUID variantId, List<UUID> variantKeyIds, List<UUID> variantValueIds) {
        super(aggregateId, timestamp);
        this.variantId = variantId;
        this.variantKeyIds = variantKeyIds;
        this.variantValueIds = variantValueIds;
    }

    public static ProductVariantAddedEvent of(String aggregateId, OffsetDateTime timestamp,
                                              UUID variantId, List<UUID> variantKeyIds, List<UUID> variantValueIds) {
        return new ProductVariantAddedEvent(aggregateId, timestamp, variantId, variantKeyIds, variantValueIds);
    }
}