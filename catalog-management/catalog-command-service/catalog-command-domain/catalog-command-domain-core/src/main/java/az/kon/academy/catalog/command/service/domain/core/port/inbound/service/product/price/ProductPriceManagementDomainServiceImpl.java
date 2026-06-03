package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceUpdateCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductPriceManagementDomainServiceImpl implements ProductPriceManagementDomainService {

    @Override
    public ProductPriceRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command) {
        return ProductPriceRoot.initialize(command);
    }

    @Override
    public ProductPriceRoot updatePrice(SeDomainContext context, ProductPriceUpdateCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.update(command);
    }
}
