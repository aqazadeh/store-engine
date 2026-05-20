package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public final class ProductAddVariantCommand {
    @Getter private final ProductId productId;
    @Getter private final List<ProductVariantAssignment> assignments;
}