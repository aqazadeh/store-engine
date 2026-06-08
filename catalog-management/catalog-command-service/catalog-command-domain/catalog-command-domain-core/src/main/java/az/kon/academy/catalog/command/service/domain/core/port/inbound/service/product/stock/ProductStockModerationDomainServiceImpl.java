package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReleaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReserveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductStockModerationDomainServiceImpl implements ProductStockModerationDomainService {

    @Override
    public ProductStockAggregateRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.increase(command);
    }

    @Override
    public ProductStockAggregateRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.decrease(command);
    }

    @Override
    public ProductStockAggregateRoot reserveStock(SeDomainContext context, ProductStockReserveCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.reserve(command);
    }

    @Override
    public ProductStockAggregateRoot releaseStock(SeDomainContext context, ProductStockReleaseCommand command) {
        var productStockQueryPort = context.getQueryPort(ProductStockQueryOutboundPort.class);
        var stock = productStockQueryPort.fetchById(command.getStockId());
        return stock.release(command);
    }
}
