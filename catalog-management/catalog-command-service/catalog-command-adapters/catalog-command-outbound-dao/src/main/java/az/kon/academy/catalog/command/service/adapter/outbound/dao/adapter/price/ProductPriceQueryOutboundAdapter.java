package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductPriceMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductPriceQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductPriceId;
import org.jooq.DSLContext;

import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_PRICE;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT;

@QueryAdapter
public class ProductPriceQueryOutboundAdapter implements ProductPriceQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductPriceMapper mapper;

    public ProductPriceQueryOutboundAdapter(DSLContext dsl, ProductPriceMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductPriceAggregateRoot> findById(ProductPriceId id) {
        return dsl.selectFrom(PRODUCT_PRICE)
                .where(PRODUCT_PRICE.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public ProductPriceAggregateRoot fetchById(ProductPriceId id) {
        return null;
    }

    @Override
    public ProductPriceAggregateRoot fetchByIdAndMerchantId(ProductPriceId id, MerchantId merchantId) {
        return null;
    }

}
