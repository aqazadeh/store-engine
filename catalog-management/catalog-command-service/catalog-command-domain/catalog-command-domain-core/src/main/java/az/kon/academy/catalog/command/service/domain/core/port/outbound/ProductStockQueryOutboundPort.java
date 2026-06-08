package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Optional;

public interface ProductStockQueryOutboundPort extends BaseQueryPort {

    Optional<ProductStockAggregateRoot> findById(ProductStockId id);

    default ProductStockAggregateRoot fetchById(ProductStockId id) {
        return this.findById(id)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.STOCK_NOT_FOUND,
                        List.of(id.value().toString())));
    }

    Optional<ProductStockAggregateRoot> findByIdAndMerchantId(ProductStockId id, MerchantId merchantId);

    default ProductStockAggregateRoot fetchByIdAndMerchantId(ProductStockId id, MerchantId merchantId) {
        return this.findByIdAndMerchantId(id, merchantId)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.STOCK_NOT_FOUND,
                        List.of(id.value().toString())));
    }
}
