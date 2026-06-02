package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.Optional;

public interface ProductCategoryQueryOutboundPort extends BaseQueryPort {

    Optional<ProductCategoryRoot> findById(ProductCategoryId productCategoryId);

    ProductCategoryRoot fetchById(ProductCategoryId productCategoryId);

    Boolean exitsByCategoryId(ProductCategoryId productCategoryId);
}
