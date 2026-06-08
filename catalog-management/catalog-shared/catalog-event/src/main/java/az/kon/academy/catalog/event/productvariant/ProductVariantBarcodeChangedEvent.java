package az.kon.academy.catalog.event.productvariant;

import az.kon.academy.event.annotation.Event;
import az.kon.academy.event.behavioral.DomainEvent;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Event(version = 1)
public final class ProductVariantBarcodeChangedEvent extends DomainEvent implements ProductVariantEvent {

    private final String barcode;

    public ProductVariantBarcodeChangedEvent(UUID eventId, String aggregateId, OffsetDateTime timestamp, Integer version,
                                             String barcode
    ) {
        super(eventId, aggregateId, timestamp, version);
        this.barcode = barcode;
    }

    private ProductVariantBarcodeChangedEvent(String aggregateId, OffsetDateTime timestamp,
                                              String barcode
    ) {
        super(aggregateId, timestamp);
        this.barcode = barcode;
    }

    public static ProductVariantBarcodeChangedEvent create(String aggregateId, OffsetDateTime timestamp,
                                                           String barcode
    ) {
        return new ProductVariantBarcodeChangedEvent(
                aggregateId, timestamp,
                barcode
        );
    }
}
