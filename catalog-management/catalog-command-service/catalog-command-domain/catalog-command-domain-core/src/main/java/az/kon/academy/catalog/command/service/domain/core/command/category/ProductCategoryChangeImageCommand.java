package az.kon.academy.catalog.command.service.domain.core.command.category;

import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductCategoryChangeImageCommand {
    @Getter private final ProductCategoryId productCategoryId;
    @Getter private final String image;
}
