package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantDiscontinueCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantMarkOutOfStockCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductVariantModerationDomainServiceImpl implements ProductVariantModerationDomainService {

    @Override
    public ProductVariantRoot markOutOfStock(SeDomainContext context, ProductVariantMarkOutOfStockCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsById(command.getProductId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.markOutOfStock();
    }

    @Override
    public ProductVariantRoot discontinue(SeDomainContext context, ProductVariantDiscontinueCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsById(command.getProductId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.discontinue();
    }

    @Override
    public ProductVariantRoot archive(SeDomainContext context, ProductVariantArchiveCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsById(command.getProductId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.archive();
    }
}
