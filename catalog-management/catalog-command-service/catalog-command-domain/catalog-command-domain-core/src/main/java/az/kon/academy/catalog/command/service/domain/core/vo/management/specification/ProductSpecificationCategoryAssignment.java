package az.kon.academy.catalog.command.service.domain.core.vo.management.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import lombok.Getter;

@Getter
public final class ProductSpecificationCategoryAssignment {
    private final ProductCategoryId categoryId;
    private final boolean isRequired;

    private ProductSpecificationCategoryAssignment(ProductCategoryId categoryId, boolean isRequired) {
        this.categoryId = categoryId;
        this.isRequired = isRequired;
    }

    public static ProductSpecificationCategoryAssignment initialize(ProductCategoryId categoryId, boolean isRequired) {
        return new ProductSpecificationCategoryAssignment(categoryId, isRequired);
    }
}
