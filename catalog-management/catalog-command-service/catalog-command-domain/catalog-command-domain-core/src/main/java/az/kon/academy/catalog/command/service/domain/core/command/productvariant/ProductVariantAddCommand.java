package az.kon.academy.catalog.command.service.domain.core.command.productvariant;

import az.kon.academy.catalog.command.service.domain.core.vo.Barcode;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantAssignment;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantSku;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public final class ProductVariantAddCommand {
    @Getter private final MerchantId merchantId;
    @Getter private final ProductId productId;
    @Getter private final List<ProductVariantAssignment> assignments;
    @Getter private final Barcode barcode;
    @Getter private final ProductVariantSku sku;
}