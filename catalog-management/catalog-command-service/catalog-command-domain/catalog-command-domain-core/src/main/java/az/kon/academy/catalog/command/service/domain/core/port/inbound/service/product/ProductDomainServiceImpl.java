package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.*;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductDomainServiceImpl implements ProductDomainService {

    @Override
    public ProductRoot createProduct(SeDomainContext context, ProductCreateCommand command) {
        return ProductRoot.initialize(command);
    }

    @Override
    public ProductRoot changeInformation(SeDomainContext context, ProductChangeInformationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.changeInformation(command);
    }

    @Override
    public ProductRoot archive(SeDomainContext context, ProductArchiveCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.archive();
    }

    @Override
    public ProductRoot assignBrand(SeDomainContext context, ProductAssignBrandCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryOutboundPort.class);
        brandQueryPort.fetchById(command.getBrandId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.assignBrand(command);
    }

    @Override
    public ProductRoot assignCategory(SeDomainContext context, ProductAssignCategoryCommand command) {
        var categoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        categoryQueryPort.fetchById(command.getCategoryId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.assignCategory(command);
    }

    @Override
    public ProductRoot assignSpecification(SeDomainContext context, ProductAssignSpecificationCommand command) {
        var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        specificationQueryPort.fetchByIdAndRowStatusActive(command.getSpecificationId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.assignSpecification(command);
    }

    @Override
    public ProductRoot removeSpecification(SeDomainContext context, ProductRemoveSpecificationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.removeSpecification(command);
    }

    @Override
    public ProductRoot addVariant(SeDomainContext context, ProductAddVariantCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.addVariant(command);
    }

    @Override
    public ProductRoot removeVariant(SeDomainContext context, ProductRemoveVariantCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndRowStatusActive(command.getProductId());
        return product.removeVariant(command);
    }

    @Override
    public ProductRejectionReasonRoot createRejectionReason(SeDomainContext context, ProductCreateRejectionReasonCommand command) {
        return ProductRejectionReasonRoot.initialize(command);
    }

    @Override
    public ProductPriceRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command) {
        return ProductPriceRoot.initialize(command);
    }

    @Override
    public ProductPriceRoot updatePrice(SeDomainContext context, ProductPriceUpdateCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.update(command);
    }

    @Override
    public ProductStockRoot createStock(SeDomainContext context, ProductStockCreateCommand command) {
        return ProductStockRoot.initialize(command);
    }

    @Override
    public ProductStockRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.increase(command);
    }

    @Override
    public ProductStockRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.decrease(command);
    }
}