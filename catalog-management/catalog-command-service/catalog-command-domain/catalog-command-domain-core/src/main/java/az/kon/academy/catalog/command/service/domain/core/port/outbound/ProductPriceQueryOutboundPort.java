package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.List;
import java.util.Optional;

public interface ProductPriceQueryOutboundPort extends BaseQueryPort {

    Optional<ProductPriceAggregateRoot> findById(ProductPriceId id);

    default ProductPriceAggregateRoot fetchById(ProductPriceId id) {
        return this.findById(id)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.PRICE_NOT_FOUND,
                        List.of(id.value().toString())));
    }
}