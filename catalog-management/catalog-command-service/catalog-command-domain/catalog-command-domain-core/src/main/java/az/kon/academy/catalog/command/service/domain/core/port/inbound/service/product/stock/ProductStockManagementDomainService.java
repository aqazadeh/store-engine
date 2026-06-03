package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockCreateCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductStockManagementDomainService extends ProductStockDomainService permits
        ProductStockManagementDomainServiceImpl {

    ProductStockRoot createStock(SeDomainContext context, ProductStockCreateCommand command);

}
