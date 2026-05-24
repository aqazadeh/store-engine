package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class ProductCategoryQueryAdapter implements ProductCategoryQueryPort {
    @Override
    public Optional<ProductCategoryRoot> findByIdAndRowStatusActive(ProductCategoryId id) {
        return Optional.empty();
    }
}
