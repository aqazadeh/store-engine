package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductPriceModerationDomainService extends ProductPriceDomainService permits
        ProductPriceModerationDomainServiceImpl {

    ProductPriceAggregateRoot changePrice(SeDomainContext context, ProductPriceChangedCommand command);

}
