package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductRemoveSpecificationCommand {
    @Getter private final ProductId productId;
    @Getter private final ProductSpecificationId specificationId;
}