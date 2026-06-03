package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockDecreaseCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductStockIncreaseCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductStockModerationDomainService extends ProductStockDomainService permits
        ProductStockModerationDomainServiceImpl {

    ProductStockRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command);

    ProductStockRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command);

}
