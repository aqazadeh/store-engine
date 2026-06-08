package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductVariantManagementDomainService extends ProductVariantDomainService permits
        ProductVariantManagementDomainServiceImpl {

    ProductVariantRoot addVariant(SeDomainContext context, ProductVariantAddCommand command);

    ProductVariantRoot removeVariant(SeDomainContext context, ProductVariantRemoveCommand command);

    ProductVariantRoot changeBarcode(SeDomainContext context, ProductVariantChangeBarcodeCommand command);

    ProductVariantRoot addImage(SeDomainContext context, ProductVariantAddImageCommand command);

    ProductVariantRoot removeImage(SeDomainContext context, ProductVariantRemoveImageCommand command);

    ProductVariantRoot markImagePrimary(SeDomainContext context, ProductVariantMarkImagePrimaryCommand command);

    ProductVariantRoot activate(SeDomainContext context, ProductVariantActivateCommand command);

    ProductVariantRoot deactivate(SeDomainContext context, ProductVariantDeactivateCommand command);

    ProductVariantRoot changeSku(SeDomainContext context, ProductVariantChangeSkuCommand command);
}
