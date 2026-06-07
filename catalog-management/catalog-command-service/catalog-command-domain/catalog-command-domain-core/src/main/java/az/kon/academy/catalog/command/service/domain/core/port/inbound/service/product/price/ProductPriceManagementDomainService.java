package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceChangedCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductPriceManagementDomainService extends ProductPriceDomainService permits ProductPriceManagementDomainServiceImpl {

    ProductPriceAggregateRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command);

    ProductPriceAggregateRoot updatePrice(SeDomainContext context, ProductPriceChangedCommand command);

}
