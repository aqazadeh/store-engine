package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductPriceManagementDomainServiceImpl implements ProductPriceManagementDomainService {

    @Override
    public ProductPriceAggregateRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        if(productQueryPort.existsByIdAndVarintIdAndMerchantId(command.getProductId(), command.getVariantId(), command.getMerchantId())){
            throw new RuntimeException("Product with id " + command.getProductId().value() + " does not exist"); // FIXME
        }
        return ProductPriceAggregateRoot.initialize(command);
    }

    @Override
    public ProductPriceAggregateRoot changePrice(SeDomainContext context, ProductPriceChangedCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.changePrice(command);
    }
}
