package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class ProductStockQueryAdapter implements ProductStockQueryPort {
    @Override
    public Optional<ProductStockRoot> findById(ProductStockId id) {
        return Optional.empty();
    }
}
