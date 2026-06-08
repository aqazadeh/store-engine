package az.kon.academy.catalog.command.service.domain.core.command.productstock;

import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductStockCreateCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductId productId;
    @Getter private final ProductVariantId variantId;
    @Getter private final Quantity quantity;
}
