package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductSpecificationValue;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductAssignSpecificationCommand {
    @Getter private final ProductId productId;
    @Getter private final ProductSpecificationId specificationId;
    @Getter private final ProductSpecificationValue value;
}