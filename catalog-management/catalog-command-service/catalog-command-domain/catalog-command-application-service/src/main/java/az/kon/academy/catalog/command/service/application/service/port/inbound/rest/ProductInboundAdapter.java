package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAddVariantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductArchiveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignBrandRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignSpecificationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductCreateRejectionReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductPriceCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductPriceUpdateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductRemoveSpecificationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductRemoveVariantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockDecreaseRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockIncreaseRequest;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductAddVariantCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductArchiveCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductAssignBrandCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductAssignCategoryCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductAssignSpecificationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductChangeInformationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductCreateRejectionReasonCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductPriceCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductPriceUpdateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductRemoveSpecificationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductRemoveVariantCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductStockCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductStockDecreaseCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.product.ProductStockIncreaseCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAddVariantCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAssignBrandCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductAssignSpecificationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRemoveSpecificationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRemoveVariantCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductName;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductSpecificationValue;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.domain.core.security.SeSecurityContextHolder;

@InputAdapter
class ProductInboundAdapter implements ProductInboundPort {

    private final SeSecurityContextHolder securityContextHolder;
    private final ProductCreateCommandHandler productCreateCommandHandler;
    private final ProductChangeInformationCommandHandler productChangeInformationCommandHandler;
    private final ProductArchiveCommandHandler productArchiveCommandHandler;
    private final ProductAssignBrandCommandHandler productAssignBrandCommandHandler;
    private final ProductAssignCategoryCommandHandler productAssignCategoryCommandHandler;
    private final ProductAssignSpecificationCommandHandler productAssignSpecificationCommandHandler;
    private final ProductRemoveSpecificationCommandHandler productRemoveSpecificationCommandHandler;
    private final ProductAddVariantCommandHandler productAddVariantCommandHandler;
    private final ProductRemoveVariantCommandHandler productRemoveVariantCommandHandler;
    private final ProductCreateRejectionReasonCommandHandler productCreateRejectionReasonCommandHandler;
    private final ProductPriceCreateCommandHandler productPriceCreateCommandHandler;
    private final ProductPriceUpdateCommandHandler productPriceUpdateCommandHandler;
    private final ProductStockCreateCommandHandler productStockCreateCommandHandler;
    private final ProductStockIncreaseCommandHandler productStockIncreaseCommandHandler;
    private final ProductStockDecreaseCommandHandler productStockDecreaseCommandHandler;

    public ProductInboundAdapter(SeSecurityContextHolder securityContextHolder,
                                 ProductCreateCommandHandler productCreateCommandHandler,
                                 ProductChangeInformationCommandHandler productChangeInformationCommandHandler,
                                 ProductArchiveCommandHandler productArchiveCommandHandler,
                                 ProductAssignBrandCommandHandler productAssignBrandCommandHandler,
                                 ProductAssignCategoryCommandHandler productAssignCategoryCommandHandler,
                                 ProductAssignSpecificationCommandHandler productAssignSpecificationCommandHandler,
                                 ProductRemoveSpecificationCommandHandler productRemoveSpecificationCommandHandler,
                                 ProductAddVariantCommandHandler productAddVariantCommandHandler,
                                 ProductRemoveVariantCommandHandler productRemoveVariantCommandHandler,
                                 ProductCreateRejectionReasonCommandHandler productCreateRejectionReasonCommandHandler,
                                 ProductPriceCreateCommandHandler productPriceCreateCommandHandler,
                                 ProductPriceUpdateCommandHandler productPriceUpdateCommandHandler,
                                 ProductStockCreateCommandHandler productStockCreateCommandHandler,
                                 ProductStockIncreaseCommandHandler productStockIncreaseCommandHandler,
                                 ProductStockDecreaseCommandHandler productStockDecreaseCommandHandler) {
        this.securityContextHolder = securityContextHolder;
        this.productCreateCommandHandler = productCreateCommandHandler;
        this.productChangeInformationCommandHandler = productChangeInformationCommandHandler;
        this.productArchiveCommandHandler = productArchiveCommandHandler;
        this.productAssignBrandCommandHandler = productAssignBrandCommandHandler;
        this.productAssignCategoryCommandHandler = productAssignCategoryCommandHandler;
        this.productAssignSpecificationCommandHandler = productAssignSpecificationCommandHandler;
        this.productRemoveSpecificationCommandHandler = productRemoveSpecificationCommandHandler;
        this.productAddVariantCommandHandler = productAddVariantCommandHandler;
        this.productRemoveVariantCommandHandler = productRemoveVariantCommandHandler;
        this.productCreateRejectionReasonCommandHandler = productCreateRejectionReasonCommandHandler;
        this.productPriceCreateCommandHandler = productPriceCreateCommandHandler;
        this.productPriceUpdateCommandHandler = productPriceUpdateCommandHandler;
        this.productStockCreateCommandHandler = productStockCreateCommandHandler;
        this.productStockIncreaseCommandHandler = productStockIncreaseCommandHandler;
        this.productStockDecreaseCommandHandler = productStockDecreaseCommandHandler;
    }

    @Override
    public void create(ProductCreateRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = ProductCreateCommand.builder()
                .merchantId(MerchantId.from(currentUser))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .brandId(BrandId.from(request.getBrandId()))
                .name(ProductName.of(request.getName()))
                .description(ProductDescription.of(request.getDescription()))
                .barcode(Barcode.of(request.getBarcode()))
                .build();
        this.productCreateCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(ProductChangeInformationRequest request) {
        var command = ProductChangeInformationCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .name(ProductName.of(request.getName()))
                .description(ProductDescription.of(request.getDescription()))
                .build();
        this.productChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void archive(ProductArchiveRequest request) {
        var command = ProductArchiveCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .build();
        this.productArchiveCommandHandler.handle(command);
    }

    @Override
    public void assignBrand(ProductAssignBrandRequest request) {
        var command = ProductAssignBrandCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .brandId(BrandId.from(request.getBrandId()))
                .build();
        this.productAssignBrandCommandHandler.handle(command);
    }

    @Override
    public void assignCategory(ProductAssignCategoryRequest request) {
        var command = ProductAssignCategoryCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .build();
        this.productAssignCategoryCommandHandler.handle(command);
    }

    @Override
    public void assignSpecification(ProductAssignSpecificationRequest request) {
        var command = ProductAssignSpecificationCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .specificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .value(ProductSpecificationValue.of(request.getValue()))
                .build();
        this.productAssignSpecificationCommandHandler.handle(command);
    }

    @Override
    public void removeSpecification(ProductRemoveSpecificationRequest request) {
        var command = ProductRemoveSpecificationCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .specificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .build();
        this.productRemoveSpecificationCommandHandler.handle(command);
    }

    @Override
    public void addVariant(ProductAddVariantRequest request) {
        var command = ProductAddVariantCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .assignments(request.getAssignments().stream()
                        .map(item -> ProductVariantAssignment.of(
                                VariantKeyId.from(item.getVariantKeyId()),
                                VariantValueId.from(item.getVariantValueId())))
                        .toList())
                .build();
        this.productAddVariantCommandHandler.handle(command);
    }

    @Override
    public void removeVariant(ProductRemoveVariantRequest request) {
        var command = ProductRemoveVariantCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .variantId(ProductVariantId.from(request.getVariantId()))
                .build();
        this.productRemoveVariantCommandHandler.handle(command);
    }

    @Override
    public void createRejectionReason(ProductCreateRejectionReasonRequest request) {
        var currentUser = this.securityContextHolder.getUser().getUserId();
        var command = ProductCreateRejectionReasonCommand.builder()
                .productId(ProductId.from(request.getProductId()))
                .reason(request.getReason())
                .moderatedBy(ModeratorId.from(currentUser))
                .build();
        this.productCreateRejectionReasonCommandHandler.handle(command);
    }

    @Override
    public void createPrice(ProductPriceCreateRequest request) {
        var command = ProductPriceCreateCommand.builder()
                .variantId(ProductVariantId.from(request.getVariantId()))
                .minPrice(Money.of(request.getMinPrice()))
                .maxPrice(Money.of(request.getMaxPrice()))
                .build();
        this.productPriceCreateCommandHandler.handle(command);
    }

    @Override
    public void updatePrice(ProductPriceUpdateRequest request) {
        var command = ProductPriceChangedCommand.builder()
                .priceId(ProductPriceId.from(request.getPriceId()))
                .minPrice(Money.of(request.getMinPrice()))
                .maxPrice(Money.of(request.getMaxPrice()))
                .build();
        this.productPriceUpdateCommandHandler.handle(command);
    }

    @Override
    public void createStock(ProductStockCreateRequest request) {
        var command = ProductStockCreateCommand.builder()
                .variantId(ProductVariantId.from(request.getVariantId()))
                .quantity(request.getQuantity())
                .build();
        this.productStockCreateCommandHandler.handle(command);
    }

    @Override
    public void increaseStock(ProductStockIncreaseRequest request) {
        var command = ProductStockIncreaseCommand.builder()
                .stockId(ProductStockId.from(request.getStockId()))
                .quantity(request.getQuantity())
                .build();
        this.productStockIncreaseCommandHandler.handle(command);
    }

    @Override
    public void decreaseStock(ProductStockDecreaseRequest request) {
        var command = ProductStockDecreaseCommand.builder()
                .stockId(ProductStockId.from(request.getStockId()))
                .quantity(request.getQuantity())
                .build();
        this.productStockDecreaseCommandHandler.handle(command);
    }
}
