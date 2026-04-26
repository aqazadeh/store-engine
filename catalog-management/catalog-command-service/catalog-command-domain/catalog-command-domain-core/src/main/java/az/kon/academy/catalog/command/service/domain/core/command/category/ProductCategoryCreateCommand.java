package az.kon.academy.catalog.command.service.domain.core.command.category;

import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ProductCategoryCreateCommand {
    @Getter
    private final ProductCategoryName name;

    @Getter
    private final ProductCategoryDescription description;

    @Getter
    private final ProductCategoryPath path;
}
