package az.kon.academy.catalog.command.service.domain.core.command.category;

import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public class ProductCategoryArchiveCommand {
    @Getter
    private final ProductCategoryId productCategoryId;
}
