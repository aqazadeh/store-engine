package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceUpdateCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductPriceManagementDomainService extends ProductPriceDomainService permits ProductPriceManagementDomainServiceImpl {

    ProductPriceRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command);

    ProductPriceRoot updatePrice(SeDomainContext context, ProductPriceUpdateCommand command);

}
