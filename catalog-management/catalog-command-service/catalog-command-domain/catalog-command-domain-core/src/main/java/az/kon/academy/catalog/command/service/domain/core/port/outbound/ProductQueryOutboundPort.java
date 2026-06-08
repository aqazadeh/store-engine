package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Optional;

public interface ProductQueryOutboundPort extends BaseQueryPort {

    Optional<ProductRoot> findById(ProductId id);

    ProductRoot fetchById(ProductId id);

    Optional<ProductRoot> findByIdAndMerchantId(ProductId id, MerchantId merchantId);
    ProductRoot fetchByIdAndMerchantId(ProductId id, MerchantId merchantId);

    Boolean existsByIdAndVarintIdAndMerchantId(ProductId productId, ProductVariantId productVariantId, MerchantId merchantId);

    Boolean exitsByCategoryId(ProductCategoryId productCategoryId);
}