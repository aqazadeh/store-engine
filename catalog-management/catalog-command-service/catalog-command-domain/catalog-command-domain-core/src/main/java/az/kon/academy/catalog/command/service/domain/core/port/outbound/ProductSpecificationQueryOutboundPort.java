package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.Optional;

public interface ProductSpecificationQueryOutboundPort extends BaseQueryPort {
    Optional<ProductSpecificationRoot> findById(ProductSpecificationId productSpecificationId);

    ProductSpecificationRoot fetchById(ProductSpecificationId productSpecificationId);

    Boolean existsAssignmentByCategoryId(ProductCategoryId categoryId);
}
