package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductStockCreateCommand {
    @Getter private final ProductVariantId variantId;
    @Getter private final Integer quantity;
}