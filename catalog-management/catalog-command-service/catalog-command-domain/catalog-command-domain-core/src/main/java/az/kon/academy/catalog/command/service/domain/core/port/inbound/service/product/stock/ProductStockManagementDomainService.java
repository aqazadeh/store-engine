package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockIncreaseCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductStockManagementDomainService extends ProductStockDomainService permits
        ProductStockManagementDomainServiceImpl {

    ProductStockAggregateRoot createStock(SeDomainContext context, ProductStockCreateCommand command);

    ProductStockAggregateRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command);

    ProductStockAggregateRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command);

}
