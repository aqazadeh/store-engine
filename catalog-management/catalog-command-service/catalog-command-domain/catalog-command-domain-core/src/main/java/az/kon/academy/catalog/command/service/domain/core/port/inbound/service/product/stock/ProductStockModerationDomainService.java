package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReleaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockReserveCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductStockModerationDomainService extends ProductStockDomainService permits
        ProductStockModerationDomainServiceImpl {

    ProductStockAggregateRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command);

    ProductStockAggregateRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command);

    ProductStockAggregateRoot reserveStock(SeDomainContext context, ProductStockReserveCommand command);

    ProductStockAggregateRoot releaseStock(SeDomainContext context, ProductStockReleaseCommand command);

}
