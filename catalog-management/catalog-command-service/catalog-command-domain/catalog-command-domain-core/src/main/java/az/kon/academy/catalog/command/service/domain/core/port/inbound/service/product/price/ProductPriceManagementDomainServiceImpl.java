package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangeActualPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceChangedCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productprice.ProductPriceToggleAutoPriceCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductPriceManagementDomainServiceImpl implements ProductPriceManagementDomainService {

    @Override
    public ProductPriceAggregateRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        productQueryPort.checkExistsByIdAndMerchantId(command.getProductId(), command.getMerchantId());

        var variantQueryPort = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        variantQueryPort.checkExistsByIdAndProductIdAndMerchantId(
                command.getVariantId(), command.getProductId(), command.getMerchantId());

        return ProductPriceAggregateRoot.initialize(command);
    }

    @Override
    public ProductPriceAggregateRoot changePrice(SeDomainContext context, ProductPriceChangedCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchByIdAndMerchantId(command.getPriceId(), command.getMerchantId());
        return price.changePrice(command);
    }

    @Override
    public ProductPriceAggregateRoot changeActualPrice(SeDomainContext context, ProductPriceChangeActualPriceCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.changeActualPrice(command);
    }

    @Override
    public ProductPriceAggregateRoot toggleAutoPriceChange(SeDomainContext context, ProductPriceToggleAutoPriceCommand command) {
        var productPriceQueryPort = context.getQueryPort(ProductPriceQueryOutboundPort.class);
        var price = productPriceQueryPort.fetchById(command.getPriceId());
        return price.toggleAutoPriceChange(command);
    }
}
