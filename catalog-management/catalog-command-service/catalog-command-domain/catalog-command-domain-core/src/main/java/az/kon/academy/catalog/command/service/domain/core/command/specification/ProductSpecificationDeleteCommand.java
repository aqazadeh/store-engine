package az.kon.academy.catalog.command.service.domain.core.command.specification;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductSpecificationDeleteCommand {
    @Getter private final ProductSpecificationId specificationId;
}
