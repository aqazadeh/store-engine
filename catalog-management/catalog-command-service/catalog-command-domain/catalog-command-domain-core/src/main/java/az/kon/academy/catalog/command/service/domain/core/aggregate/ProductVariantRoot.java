package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.event.productvariant.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductVariantRoot extends AggregateRoot<ProductVariantRoot, ProductVariantId> {

    @Getter private final ProductId productId;
    @Getter private final List<ProductVariantAssignment> assignments;
    @Getter private final Barcode barcode;
    @Getter private final List<String> images;


    public static ProductVariantRoot initialize(ProductVariantAddCommand command) {

        var productVariant = ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .assignments(List.copyOf(command.getAssignments()))
                .barcode(command.getBarcode())
                .build();

        var event = ProductVariantAddedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getProductId().value(),
                productVariant.getAssignments().stream().map(a -> a.getVariantKeyId().value()).toList(),
                productVariant.getAssignments().stream().map(a -> a.getVariantValueId().value()).toList(),
                productVariant.getBarcode().value()
        );

        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot changeBarcode(Barcode barcode) {
        var productVariant = this.toBuilder()
                .barcode(barcode)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantBarcodeChangedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getBarcode().value()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot addImage(String image) {
        var changed = new ArrayList<>(this.images);
        changed.add(image);
        var productVariant = this.toBuilder()
                .images(Collections.unmodifiableList(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantImageAddedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                image
        );

        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot removeImage(String image) {
        var changed = new ArrayList<>(this.images);
        changed.removeIf(i -> i.equals(image));
        var productVariant = this.toBuilder()
                .images(Collections.unmodifiableList(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantImageRemovedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                image
        );

        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot markImagePrimary(String image) {
        var changed = new ArrayList<>(this.images);
        changed.removeIf(i -> i.equals(image));
        changed.addFirst(image);
        var productVariant = this.toBuilder()
                .images(Collections.unmodifiableList(changed))
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantImageMarkedAsPrimaryEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                image
        );

        productVariant.addEvent(event);
        return productVariant;
    }
}