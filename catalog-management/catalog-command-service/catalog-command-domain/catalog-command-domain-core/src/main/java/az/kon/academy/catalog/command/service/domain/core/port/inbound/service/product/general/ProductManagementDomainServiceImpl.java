package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class ProductManagementDomainServiceImpl implements ProductManagementDomainService {

    @Override
    public ProductRoot createProduct(SeDomainContext context, ProductCreateCommand command) {
        var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        productCategoryQuery.checkExitsById(command.getCategoryId());

        var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        if(brandQuery.exitsById(command.getBrandId())) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.BRAND_NOT_FOUND,
                    List.of(command.getCategoryId().toString())
            );
        }

        return ProductRoot.initialize(command);
    }

    @Override
    public ProductRoot changeInformation(SeDomainContext context, ProductChangeInformationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.changeInformation(command);
    }

    @Override
    public ProductRoot archive(SeDomainContext context, ProductArchiveCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.archive();
    }

    @Override
    public ProductRoot assignBrand(SeDomainContext context, ProductAssignBrandCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryOutboundPort.class);
        brandQueryPort.fetchById(command.getBrandId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.assignBrand(command);
    }

    @Override
    public ProductRoot assignCategory(SeDomainContext context, ProductAssignCategoryCommand command) {
        var categoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        categoryQueryPort.fetchById(command.getCategoryId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.assignCategory(command);
    }

    @Override
    public ProductRoot assignSpecification(SeDomainContext context, ProductAssignSpecificationCommand command) {
        var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        specificationQueryPort.fetchById(command.getSpecificationId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.assignSpecification(command);
    }

    @Override
    public ProductRoot removeSpecification(SeDomainContext context, ProductRemoveSpecificationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.removeSpecification(command);
    }

    @Override
    public ProductRoot addVariant(SeDomainContext context, ProductVariantAddCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.addVariant(command);
    }

    @Override
    public ProductRoot removeVariant(SeDomainContext context, ProductVariantRemoveCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.removeVariant(command);
    }

    @Override
    public ProductRoot sentToApproval(SeDomainContext context, ProductSentToApprovalCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.sentToApproval();
    }

    @Override
    public ProductRoot moveToDraft(SeDomainContext context, ProductMoveToDraftCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchById(command.getProductId());
        return product.moveToDraft();
    }
}