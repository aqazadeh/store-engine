package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductStockManagementDomainServiceImpl implements ProductStockManagementDomainService {

    @Override
    public ProductStockAggregateRoot createStock(SeDomainContext context, ProductStockCreateCommand command) {
        var variantQueryPort = context.getQueryPort(ProductVariantQueryOutboundPort.class);
        variantQueryPort.fetchByIdAndProductIdAndMerchantId(
                command.getVariantId(), command.getProductId(), command.getMerchantId());
        return ProductStockAggregateRoot.initialize(command);
    }

    @Override
    public ProductStockAggregateRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchByIdAndMerchantId(command.getStockId(), command.getMerchantId());
        return stock.increase(command);
    }

    @Override
    public ProductStockAggregateRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchByIdAndMerchantId(command.getStockId(), command.getMerchantId());
        return stock.decrease(command);
    }
}
