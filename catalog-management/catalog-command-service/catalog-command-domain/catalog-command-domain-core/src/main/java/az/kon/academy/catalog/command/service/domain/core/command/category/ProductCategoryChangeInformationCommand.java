package az.kon.academy.catalog.command.service.domain.core.command.category;

import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryPath;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ProductCategoryChangeInformationCommand {
    @Getter
    private ProductCategoryId productCategoryId;

    @Getter
    private final ProductCategoryName name;

    @Getter
    private final ProductCategoryDescription description;

    @Getter
    private final ProductCategoryPath path;
}
