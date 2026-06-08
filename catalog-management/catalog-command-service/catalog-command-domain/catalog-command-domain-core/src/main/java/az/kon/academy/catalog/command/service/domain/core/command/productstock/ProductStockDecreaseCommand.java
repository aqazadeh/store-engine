package az.kon.academy.catalog.command.service.domain.core.command.productstock;

import az.kon.academy.aggragate.valueobject.Quantity;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductStockDecreaseCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductStockId stockId;
    @Getter private final Quantity quantity;
}
