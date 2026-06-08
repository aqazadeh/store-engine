package az.kon.academy.catalog.command.service.domain.core.command.productprice;

import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductPriceToggleAutoPriceCommand {
    @Getter private final ProductPriceId priceId;
    @Getter private final Boolean enabled;
}
