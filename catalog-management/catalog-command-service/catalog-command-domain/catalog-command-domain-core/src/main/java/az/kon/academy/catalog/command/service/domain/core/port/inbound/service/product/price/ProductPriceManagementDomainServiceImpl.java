package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductPriceManagementDomainServiceImpl implements ProductPriceManagementDomainService {

    @Override
    public ProductPriceAggregateRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command) {
        return ProductPriceAggregateRoot.initialize(command);
    }

    @Override
    public ProductPriceAggregateRoot updatePrice(SeDomainContext context, ProductPriceChangedCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.update(command);
    }
}
