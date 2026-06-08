package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;

@QueryAdapter
public class ProductVariantQueryOutboundAdapter implements ProductVariantQueryOutboundPort {
    @Override
    public ProductVariantRoot fetchByIdAndProductIdAndMerchantId(ProductVariantId variantId, ProductId productId, MerchantId merchantId) {
        return null;
    }

    @Override
    public void checkExistsByIdAndProductIdAndMerchantId(ProductVariantId variantId, ProductId productId, MerchantId merchantId) {

    }
}
