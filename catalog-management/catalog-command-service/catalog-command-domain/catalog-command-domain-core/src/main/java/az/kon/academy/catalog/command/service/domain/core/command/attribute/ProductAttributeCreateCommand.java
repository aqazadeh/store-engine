package az.kon.academy.catalog.command.service.domain.core.command.attribute;

import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeName;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductAttributeCreateCommand {
    @Getter private final ProductCategoryId categoryId;
    @Getter private final ProductAttributeName name;
    @Getter private final ProductAttributeDescription description;
    @Getter private final Boolean isRequired;
}
