package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductPriceMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_PRICE;

@QueryAdapter
public class ProductPriceQueryAdapter implements ProductPriceQueryPort {

    private final DSLContext dsl;
    private final ProductPriceMapper mapper;

    public ProductPriceQueryAdapter(DSLContext dsl, ProductPriceMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductPriceRoot> findById(ProductPriceId id) {
        return dsl.selectFrom(PRODUCT_PRICE)
                .where(PRODUCT_PRICE.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }
}
