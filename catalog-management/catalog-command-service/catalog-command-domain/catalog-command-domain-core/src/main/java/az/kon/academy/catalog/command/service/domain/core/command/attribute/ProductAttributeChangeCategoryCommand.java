package az.kon.academy.catalog.command.service.domain.core.command.attribute;

import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeId;
import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeName;
import az.kon.academy.catalog.command.service.domain.core.vo.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductAttributeChangeCategoryCommand {
    @Getter private final ProductAttributeId attributeId;
    @Getter private final ProductCategoryId categoryId;
}
