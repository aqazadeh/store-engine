package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component // FIXME change to custom annotation @QueryAdapter
public class ProductQueryAdapter implements ProductQueryPort {
    @Override
    public Optional<ProductRoot> findByIdAndRowStatusActive(ProductId id) {
        return Optional.empty();
    }
}
