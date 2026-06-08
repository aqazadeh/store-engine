package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.domain.core.BaseQueryPort;

public interface ProductVariantQueryOutboundPort extends BaseQueryPort {

    ProductVariantRoot fetchByIdAndProductIdAndMerchantId(ProductVariantId variantId, ProductId productId, MerchantId merchantId);
}
