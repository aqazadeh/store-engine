package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.SpecificationDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.SpecificationEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Optional;

public interface ProductSpecificationQueryOutboundPort extends BaseQueryPort {
    Optional<ProductSpecificationRoot> findByIdAndRowStatusActive(ProductSpecificationId id);

    default ProductSpecificationRoot fetchByIdAndRowStatusActive(ProductSpecificationId id) {
        return this.findByIdAndRowStatusActive(id)
                .orElseThrow(() -> new SpecificationEntityNotFoundException(
                        SpecificationDomainErrorCodes.ENTITY_NOT_FOUND,
                        List.of(id.toString()))
                );
    }
}
