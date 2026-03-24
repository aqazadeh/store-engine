package az.kon.academy.catalog.command.service.domain.core.command.attribute;

import az.kon.academy.catalog.command.service.domain.core.vo.attribute.ProductAttributeId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductAttributeMarkAsNonRequiredCommand {
    @Getter private final ProductAttributeId attributeId;
}
