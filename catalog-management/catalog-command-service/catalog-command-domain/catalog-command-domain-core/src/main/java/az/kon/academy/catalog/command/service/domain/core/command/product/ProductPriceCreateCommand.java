package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.aggragate.valueobject.Money;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductPriceCreateCommand {
    @Getter private final ProductVariantId variantId;
    @Getter private final Money minPrice;
    @Getter private final Money maxPrice;
    @Getter private final Money defaultPrice;
    @Getter private final Boolean autoPriceUpdateEnabled;
}