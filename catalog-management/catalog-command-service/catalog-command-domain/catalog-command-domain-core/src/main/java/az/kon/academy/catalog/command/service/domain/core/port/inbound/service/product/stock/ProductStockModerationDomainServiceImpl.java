package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductStockModerationDomainServiceImpl implements ProductStockModerationDomainService {

    @Override
    public ProductStockRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.increase(command);
    }

    @Override
    public ProductStockRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.decrease(command);
    }
}
