package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class ProductPriceQueryAdapter implements ProductPriceQueryPort {
    @Override
    public Optional<ProductPriceRoot> findById(ProductPriceId id) {
        return Optional.empty();
    }
}
