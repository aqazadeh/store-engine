package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantArchiveCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantDiscontinueCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantMarkOutOfStockCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductVariantModerationDomainService extends ProductVariantDomainService permits
        ProductVariantModerationDomainServiceImpl {

    ProductVariantRoot markOutOfStock(SeDomainContext context, ProductVariantMarkOutOfStockCommand command);

    ProductVariantRoot discontinue(SeDomainContext context, ProductVariantDiscontinueCommand command);

    ProductVariantRoot archive(SeDomainContext context, ProductVariantArchiveCommand command);
}
