package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.SpecificationQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class ProductSpecificationQueryAdapter implements SpecificationQueryPort {
    @Override
    public Optional<ProductSpecificationRoot> findByIdAndRowStatusActive(ProductSpecificationId id) {
        return Optional.empty();
    }
}
