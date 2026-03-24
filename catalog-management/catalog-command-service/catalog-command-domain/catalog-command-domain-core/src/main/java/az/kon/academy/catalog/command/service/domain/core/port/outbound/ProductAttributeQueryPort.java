package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductAttributeRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.attribute.ProductAttributeDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.attribute.ProductAttributeEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeId;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeQueryPort {
    Optional<ProductAttributeRoot> findByIdAndRowStatusActive(ProductAttributeId id);

    default ProductAttributeRoot fetchByIdAndRowStatusActive(ProductAttributeId id) {
        return this.findByIdAndRowStatusActive(id)
                .orElseThrow(() -> new ProductAttributeEntityNotFoundException(
                        ProductAttributeDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(id.toString()))
                );
    }
}
