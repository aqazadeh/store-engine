package az.kon.academy.catalog.command.service.domain.core.aggregate;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.aggragate.valueobject.SeDateTime;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.*;
import az.kon.academy.catalog.event.product.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder(toBuilder = true)
public class ProductRoot extends AggregateRoot<ProductRoot, ProductId> {

    @Getter private final MerchantId merchantId;
    @Getter private ProductCategoryId categoryId;
    @Getter private BrandId brandId;
    @Getter private ProductName name;
    @Getter private ProductDescription description;
    @Getter private Boolean autoPriceUpdateEnabled;
    @Getter private ProductStatus status;
    @Getter private List<ProductSpecificationAssignment> specifications;
    @Getter private List<ProductVariantRoot> variants;

    public static ProductRoot initialize(ProductCreateCommand command) {
        var product = ProductRoot.builder()
                .id(ProductId.random())
                .merchantId(command.getMerchantId())
                .categoryId(command.getCategoryId())
                .brandId(command.getBrandId())
                .name(command.getName())
                .description(command.getDescription())
                .autoPriceUpdateEnabled(Boolean.FALSE)
                .status(ProductStatus.DRAFT)
                .specifications(Collections.emptyList())
                .variants(Collections.emptyList())
                .build();

        var event = ProductCreatedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getMerchantId().value(),
                product.getCategoryId().value(),
                product.getBrandId().value(),
                product.getName().value(),
                product.getDescription().value(),
                product.getStatus().name()
        );

        product.addEvent(event);
        return product;
    }

    public ProductRoot sentToApproval() {
        if (!this.status.isDraft() && !this.status.isRejected()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.STATUS_INVALID_FOR_APPROVAL,
                    List.of(this.getRootID().toString())
            );
        }

        var product = this.toBuilder()
                .status(ProductStatus.SENT_TO_APPROVAL)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductSentToApprovalEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot approve() {
        var product = this.toBuilder()
                .status(ProductStatus.APPROVED)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductApprovedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot reject() {
        var product = this.toBuilder()
                .status(ProductStatus.REJECTED)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductRejectedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot moveToDraft() {
        if (!this.status.isSentToApproval()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.STATUS_INVALID_FOR_MOVE_TO_DRAFT,
                    List.of(this.getRootID().toString())
            );
        }

        var product = this.toBuilder()
                .status(ProductStatus.DRAFT)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductMovedToDraftEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot moveToInReview() {
        if (!this.status.isSentToApproval()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.STATUS_INVALID_FOR_MOVE_TO_IN_REVIEW,
                    List.of(this.getRootID().toString())
            );
        }

        var product = this.toBuilder()
                .status(ProductStatus.IN_REVIEW)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductMovedToInReviewEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot archive() {
        if (!this.status.isApproved()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.STATUS_INVALID_FOR_ARCHIVE,
                    List.of(this.getRootID().toString())
            );
        }

        var product = this.toBuilder()
                .status(ProductStatus.ARCHIVED)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductArchivedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getStatus().name()
        ));
        return product;
    }

    public ProductRoot changeInformation(ProductChangeInformationCommand command) {
        if (this.status.isInReview()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.CANNOT_BE_CHANGED_WHEN_IN_REVIEW,
                    List.of(this.getRootID().toString())
            );
        }

        var product = this.toBuilder()
                .name(command.getName())
                .description(command.getDescription())
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductInformationChangedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getName().value(),
                product.getDescription().value()
        ));
        return product;
    }

    public ProductRoot assignCategory(ProductAssignCategoryCommand command) {
        var product = this.toBuilder()
                .categoryId(command.getCategoryId())
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductCategoryAssignedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getCategoryId().value()
        ));
        return product;
    }

    public ProductRoot assignBrand(ProductAssignBrandCommand command) {
        var product = this.toBuilder()
                .brandId(command.getBrandId())
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductBrandAssignedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                product.getBrandId().value()
        ));
        return product;
    }

    public ProductRoot assignSpecification(ProductAssignSpecificationCommand command) {
        var changed = new ArrayList<>(this.specifications);
        changed.removeIf(s -> command.getSpecificationId().equals(s.getSpecificationId()));
        changed.add(ProductSpecificationAssignment.of(command.getSpecificationId(), command.getValue()));

        var product = this.toBuilder()
                .specifications(Collections.unmodifiableList(changed))
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductSpecificationAssignedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                command.getSpecificationId().value(),
                command.getValue().value()
        ));
        return product;
    }

    public ProductRoot removeSpecification(ProductRemoveSpecificationCommand command) {
        var changed = new ArrayList<>(this.specifications);
        changed.removeIf(s -> command.getSpecificationId().equals(s.getSpecificationId()));

        var product = this.toBuilder()
                .specifications(Collections.unmodifiableList(changed))
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductSpecificationRemovedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                command.getSpecificationId().value()
        ));
        return product;
    }

    public ProductRoot addVariant(ProductVariantAddCommand command) {
        var variant = ProductVariantRoot.initialize(command);

        var updatedVariants = new ArrayList<>(this.variants);
        updatedVariants.add(variant);

        var product = this.toBuilder()
                .variants(Collections.unmodifiableList(updatedVariants))
                .modificationTs(SeDateTime.now())
                .build();

        var keyIds = command.getAssignments().stream()
                .map(a -> a.getVariantKeyId().value())
                .toList();
        var valueIds = command.getAssignments().stream()
                .map(a -> a.getVariantValueId().value())
                .toList();

        product.addEvent(ProductVariantAddedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                variant.getRootID().value(),
                keyIds,
                valueIds
        ));
        return product;
    }

    public ProductRoot removeVariant(ProductVariantRemoveCommand command) {
        var updatedVariants = this.variants.stream()
                .filter(v -> !command.getVariantId().value().equals(v.getRootID().value()))
                .toList();

        var product = this.toBuilder()
                .variants(updatedVariants)
                .modificationTs(SeDateTime.now())
                .build();

        product.addEvent(ProductVariantRemovedEvent.of(
                product.getRootID().value().toString(),
                product.getModificationTs().toOffsetDateTime(),
                command.getVariantId().value()
        ));
        return product;
    }
}