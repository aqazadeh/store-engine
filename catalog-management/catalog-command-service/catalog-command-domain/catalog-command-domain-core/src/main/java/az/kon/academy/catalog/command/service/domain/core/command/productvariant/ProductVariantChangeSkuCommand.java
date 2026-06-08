package az.kon.academy.catalog.command.service.domain.core.command.productvariant;

import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantSku;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductVariantChangeSkuCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductId productId;
    @Getter private final ProductVariantId productVariantId;
    @Getter private final ProductVariantSku sku;
}
