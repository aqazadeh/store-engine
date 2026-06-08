package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.*;

public sealed interface ProductVariantManagementDomainService extends ProductVariantDomainService permits
        ProductVariantManagementDomainServiceImpl {

    ProductVariantRoot addVariant(ProductVariantAddCommand command);

    ProductVariantRoot removeVariant(ProductVariantRemoveCommand command);

    ProductVariantRoot changeBarcode(ProductVariantChangeBarcodeCommand command);

    ProductVariantRoot addImage(ProductVariantAddImageCommand command);

    ProductVariantRoot removeImage(ProductVariantRemoveImageCommand command);

    ProductVariantRoot markImagePrimary(ProductVariantMarkImagePrimaryCommand command);
}
