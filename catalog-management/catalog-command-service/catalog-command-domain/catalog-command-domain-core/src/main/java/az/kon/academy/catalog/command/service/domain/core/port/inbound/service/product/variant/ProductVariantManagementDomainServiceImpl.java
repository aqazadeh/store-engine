package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductVariantManagementDomainServiceImpl implements ProductVariantManagementDomainService {

    @Override
    public ProductVariantRoot addVariant(SeDomainContext context, ProductVariantAddCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return ProductVariantRoot.initialize(command);
    }

    @Override
    public ProductVariantRoot removeVariant(SeDomainContext context, ProductVariantRemoveCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getVariantId(), command.getProductId(), command.getMerchantId());
        return variant.remove();
    }

    @Override
    public ProductVariantRoot changeBarcode(SeDomainContext context, ProductVariantChangeBarcodeCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.changeBarcode(command.getBarcode());
    }

    @Override
    public ProductVariantRoot addImage(SeDomainContext context, ProductVariantAddImageCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.addImage(command.getImage());
    }

    @Override
    public ProductVariantRoot removeImage(SeDomainContext context, ProductVariantRemoveImageCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.removeImage(command.getImage());
    }

    @Override
    public ProductVariantRoot markImagePrimary(SeDomainContext context, ProductVariantMarkImagePrimaryCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.markImagePrimary(command.getImage());
    }

    @Override
    public ProductVariantRoot activate(SeDomainContext context, ProductVariantActivateCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.activate();
    }

    @Override
    public ProductVariantRoot deactivate(SeDomainContext context, ProductVariantDeactivateCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.deactivate();
    }

    @Override
    public ProductVariantRoot changeSku(SeDomainContext context, ProductVariantChangeSkuCommand command) {
        var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        productQuery.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        var variantQuery = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        var variant = variantQuery.fetchByIdAndProductIdAndMerchantId(
                command.getProductVariantId(), command.getProductId(), command.getMerchantId());
        return variant.changeSku(command.getSku());
    }
}
