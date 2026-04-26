package az.kon.academy.catalog.command.service.domain.core.vo.management.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import lombok.Getter;

@Getter
public final class SpecificationCategoryAssignment {
    private final ProductCategoryId categoryId;
    private final boolean isRequired;

    private SpecificationCategoryAssignment(ProductCategoryId categoryId, boolean isRequired) {
        this.categoryId = categoryId;
        this.isRequired = isRequired;
    }

    public static SpecificationCategoryAssignment initialize(ProductCategoryId categoryId, boolean isRequired) {
        return new SpecificationCategoryAssignment(categoryId, isRequired);
    }
}
