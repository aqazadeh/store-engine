package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductStockMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_STOCK;

@Component
@RequiredArgsConstructor
public class ProductStockQueryAdapter implements ProductStockQueryPort {

    private final DSLContext dsl;
    private final ProductStockMapper mapper;

    @Override
    public Optional<ProductStockRoot> findById(ProductStockId id) {
        return dsl.selectFrom(PRODUCT_STOCK)
                .where(PRODUCT_STOCK.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
