package az.kon.academy.catalog.command.service.domain.core.command.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductSpecificationRemoveCategoryAssignmentCommand {
    @Getter private final ProductSpecificationId productSpecificationId;
    @Getter private final ProductCategoryId categoryId;
}
