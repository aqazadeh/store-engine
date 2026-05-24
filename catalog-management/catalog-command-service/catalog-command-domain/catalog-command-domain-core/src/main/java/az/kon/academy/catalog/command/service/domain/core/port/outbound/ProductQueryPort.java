package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductQueryPort {

    Optional<ProductRoot> findByIdAndRowStatusActive(ProductId id);

    default ProductRoot fetchByIdAndRowStatusActive(ProductId id) {
        return this.findByIdAndRowStatusActive(id)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(id.value().toString())));
    }
}