package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangeActualPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceToggleAutoPriceCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductPriceManagementDomainService extends ProductPriceDomainService permits ProductPriceManagementDomainServiceImpl {

    ProductPriceAggregateRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command);

    ProductPriceAggregateRoot changePrice(SeDomainContext context, ProductPriceChangedCommand command);

    ProductPriceAggregateRoot changeActualPrice(SeDomainContext context, ProductPriceChangeActualPriceCommand command);

    ProductPriceAggregateRoot toggleAutoPriceChange(SeDomainContext context, ProductPriceToggleAutoPriceCommand command);

}
