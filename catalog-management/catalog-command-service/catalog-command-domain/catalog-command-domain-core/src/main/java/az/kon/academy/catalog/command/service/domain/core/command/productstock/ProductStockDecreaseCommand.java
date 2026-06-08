package az.kon.academy.catalog.command.service.domain.core.command.productstock;

import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductStockDecreaseCommand {
    @Getter private final ProductStockId stockId;
    @Getter private final Integer quantity;
}