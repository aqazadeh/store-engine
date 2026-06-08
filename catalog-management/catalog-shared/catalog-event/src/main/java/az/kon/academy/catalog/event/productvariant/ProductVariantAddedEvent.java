package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantAddedEvent extends DomainEvent implements ProductVariantEvent {
    private final UUID productId;
    private final List<UUID> variantIds;
    private final List<UUID> variantValueIds;
    private final String barcode;
    private final String status;
    private final String sku;

    public ProductVariantAddedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                    UUID productId,
                                    List<UUID> variantIds,
                                    List<UUID> variantValueIds,
                                    String barcode,
                                    String status,
                                    String sku
    ) {
        super(eventId, aggregateId, timestamp, version);
        this.productId = productId;
        this.variantIds = variantIds;
        this.variantValueIds = variantValueIds;
        this.barcode = barcode;
        this.status = status;
        this.sku = sku;
    }

    private ProductVariantAddedEvent(String aggregateId, OffsetDateTime timestamp,
                                     UUID productId,
                                     List<UUID> variantIds,
                                     List<UUID> variantValueIds,
                                     String barcode,
                                     String status,
                                     String sku
    ) {
        super(aggregateId, timestamp);
        this.productId = productId;
        this.variantIds = variantIds;
        this.variantValueIds = variantValueIds;
        this.barcode = barcode;
        this.status = status;
        this.sku = sku;
    }

    public static ProductVariantAddedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                  UUID productId,
                                                  List<UUID> variantIds,
                                                  List<UUID> variantValueIds,
                                                  String barcode,
                                                  String status,
                                                  String sku
    ) {
        return new ProductVariantAddedEvent(
                aggregateId, timestamp,
                productId,
                variantIds,
                variantValueIds,
                barcode,
                status,
                sku
        );
    }
}
