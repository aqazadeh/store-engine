package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductPriceModerationDomainServiceImpl implements ProductPriceModerationDomainService {

    @Override
    public ProductPriceAggregateRoot changePrice(SeDomainContext context, ProductPriceChangedCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.changePrice(command);
    }
}
