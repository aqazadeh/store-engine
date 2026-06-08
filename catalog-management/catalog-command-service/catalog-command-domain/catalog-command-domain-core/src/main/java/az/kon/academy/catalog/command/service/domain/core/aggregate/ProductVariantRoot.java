package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantSku;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantStatus;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductVariantDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductVariantDomainException;
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
    @Getter private final ProductVariantSku sku;
    @Getter private final List<String> images;
    @Getter private final ProductVariantStatus status;


    public static ProductVariantRoot initialize(ProductVariantAddCommand command) {

        var productVariant = ProductVariantRoot.builder()
                .id(ProductVariantId.random())
                .productId(command.getProductId())
                .assignments(List.copyOf(command.getAssignments()))
                .barcode(command.getBarcode())
                .sku(command.getSku())
                .images(List.of())
                .status(ProductVariantStatus.DRAFT)
                .build();

        var event = ProductVariantAddedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getProductId().value(),
                productVariant.getAssignments().stream().map(a -> a.getVariantKeyId().value()).toList(),
                productVariant.getAssignments().stream().map(a -> a.getVariantValueId().value()).toList(),
                productVariant.getBarcode().value(),
                productVariant.getStatus().name(),
                productVariant.getSku().value()
        );

        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot changeBarcode(Barcode barcode) {
        if (!this.status.isDraft()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_CHANGED,
                    List.of(this.getRootID().toString())
            );
        }

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
        if (this.status.isArchived() || this.status.isDiscontinued()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_CHANGED,
                    List.of(this.getRootID().toString())
            );
        }

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
        if (this.status.isArchived() || this.status.isDiscontinued()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_CHANGED,
                    List.of(this.getRootID().toString())
            );
        }

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
        if (this.status.isArchived() || this.status.isDiscontinued()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_CHANGED,
                    List.of(this.getRootID().toString())
            );
        }

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

    public ProductVariantRoot activate() {
        if (!this.status.isDraft() && !this.status.isInactive() && !this.status.isOutOfStock()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.STATUS_INVALID_FOR_ACTIVATE,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .status(ProductVariantStatus.ACTIVE)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantActivatedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getStatus().name()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot deactivate() {
        if (!this.status.isActive()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.STATUS_INVALID_FOR_DEACTIVATE,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .status(ProductVariantStatus.INACTIVE)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantDeactivatedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getStatus().name()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot markOutOfStock() {
        if (!this.status.isActive()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.STATUS_INVALID_FOR_OUT_OF_STOCK,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .status(ProductVariantStatus.OUT_OF_STOCK)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantMarkedOutOfStockEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getStatus().name()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot discontinue() {
        if (this.status.isArchived() || this.status.isDiscontinued()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.STATUS_INVALID_FOR_DISCONTINUE,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .status(ProductVariantStatus.DISCONTINUED)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantDiscontinuedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getStatus().name()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot archive() {
        if (!this.status.isDiscontinued()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.STATUS_INVALID_FOR_ARCHIVE,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .status(ProductVariantStatus.ARCHIVED)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantArchivedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime()
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot changeSku(ProductVariantSku sku) {
        if (!this.status.isDraft()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_CHANGED,
                    List.of(this.getRootID().toString())
            );
        }

        var productVariant = this.toBuilder()
                .sku(sku)
                .modificationTs(SeDateTime.now())
                .build();

        var event = ProductVariantSkuChangedEvent.create(
                productVariant.getRootID().value().toString(),
                productVariant.getModificationTs().toOffsetDateTime(),
                productVariant.getSku() != null ? productVariant.getSku().value() : null
        );
        productVariant.addEvent(event);
        return productVariant;
    }

    public ProductVariantRoot remove() {
        if (!this.status.isDraft()) {
            throw new ProductVariantDomainException(
                    ProductVariantDomainErrorCodes.ONLY_DRAFT_CAN_BE_REMOVED,
                    List.of(this.getRootID().toString())
            );
        }

        var event = ProductVariantRemovedEvent.create(
                getRootID().value().toString(),
                getModificationTs().toOffsetDateTime()
        );
        addEvent(event);
        return this;
    }
}