package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductStockMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import org.jooq.DSLContext;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_STOCK;

@QueryAdapter
public class ProductStockQueryOutboundAdapter implements ProductStockQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductStockMapper mapper;

    public ProductStockQueryOutboundAdapter(DSLContext dsl, ProductStockMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductStockRoot> findById(ProductStockId id) {
        return dsl.selectFrom(PRODUCT_STOCK)
                .where(PRODUCT_STOCK.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
