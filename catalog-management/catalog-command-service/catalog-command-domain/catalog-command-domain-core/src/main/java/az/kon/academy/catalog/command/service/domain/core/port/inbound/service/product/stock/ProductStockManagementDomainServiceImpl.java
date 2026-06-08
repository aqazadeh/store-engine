package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.command.productstock.ProductStockCreateCommand;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductStockManagementDomainServiceImpl implements ProductStockManagementDomainService {

    @Override
    public ProductStockAggregateRoot createStock(SeDomainContext context, ProductStockCreateCommand command) {
        return ProductStockAggregateRoot.initialize(command);
    }

}
