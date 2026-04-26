package az.kon.academy.catalog.command.service.domain.core.command.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class SpecificationCreateCommand {
    @Getter private final SpecificationName name;
    @Getter private final SpecificationDescription description;
}
