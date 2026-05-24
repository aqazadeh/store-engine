package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;

import java.util.List;
import java.util.Optional;

public interface ProductStockQueryPort {

    Optional<ProductStockRoot> findById(ProductStockId id);

    default ProductStockRoot fetchById(ProductStockId id) {
        return this.findById(id)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.STOCK_NOT_FOUND,
                        List.of(id.value().toString())));
    }
}