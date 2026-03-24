package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryQueryPort {

    Optional<ProductCategoryRoot> findByIdAndRowStatusActive(ProductCategoryId id);

    default ProductCategoryRoot fetchByIdAndRowStatusActive(ProductCategoryId id) {
        return this.findByIdAndRowStatusActive(id).orElseThrow(() -> new ProductCategoryEntityNotFoundException(
                        ProductCategoryDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(id.value().toString()))
                );
    }
}
