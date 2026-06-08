package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductStockMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductStockQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductStockId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_STOCK;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT;

@QueryAdapter
public class ProductStockQueryOutboundAdapter implements ProductStockQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductStockMapper mapper;

    public ProductStockQueryOutboundAdapter(DSLContext dsl, ProductStockMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProductStockAggregateRoot> findById(ProductStockId id) {
        return dsl.selectFrom(PRODUCT_STOCK)
                .where(PRODUCT_STOCK.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public ProductStockAggregateRoot fetchById(ProductStockId id) {
        return this.findById(id)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.STOCK_NOT_FOUND, List.of(id.toString())));
    }

    @Override
    public Optional<ProductStockAggregateRoot> findByIdAndMerchantId(ProductStockId id, MerchantId merchantId) {
        return dsl.select(PRODUCT_STOCK.fields())
                .from(PRODUCT_STOCK)
                .join(PRODUCT_VARIANT).on(PRODUCT_STOCK.VARIANT_ID.eq(PRODUCT_VARIANT.ID))
                .join(PRODUCT).on(PRODUCT_VARIANT.PRODUCT_ID.eq(PRODUCT.ID))
                .where(PRODUCT_STOCK.ID.eq(id.value())
                        .and(PRODUCT.MERCHANT_ID.eq(merchantId.value())))
                .fetchOptional()
                .map(r -> mapper.toDomain(r.into(PRODUCT_STOCK)));
    }

    @Override
    public ProductStockAggregateRoot fetchByIdAndMerchantId(ProductStockId id, MerchantId merchantId) {
        return this.findByIdAndMerchantId(id, merchantId)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.STOCK_NOT_FOUND, List.of(id.toString())));
    }
}
