package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.*;

public final class ProductVariantManagementDomainServiceImpl implements ProductVariantManagementDomainService {
    @Override
    public ProductVariantRoot addVariant(ProductVariantAddCommand command) {
        return ProductVariantRoot.initialize(command);
    }

    @Override
    public ProductVariantRoot removeVariant(ProductVariantRemoveCommand command) {
        return null;
    }

    @Override
    public ProductVariantRoot changeBarcode(ProductVariantChangeBarcodeCommand command) {
        return null;
    }

    @Override
    public ProductVariantRoot addImage(ProductVariantAddImageCommand command) {
        return null;
    }

    @Override
    public ProductVariantRoot removeImage(ProductVariantRemoveImageCommand command) {
        return null;
    }

    @Override
    public ProductVariantRoot markImagePrimary(ProductVariantMarkImagePrimaryCommand command) {
        return null;
    }
}
