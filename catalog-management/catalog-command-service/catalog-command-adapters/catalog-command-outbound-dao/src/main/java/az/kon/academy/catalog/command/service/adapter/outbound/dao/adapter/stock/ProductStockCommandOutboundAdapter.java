package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductStockMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockAggregateRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_STOCK;

@CommandAdapter
public class ProductStockCommandOutboundAdapter implements ProductStockCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductStockMapper mapper;

    public ProductStockCommandOutboundAdapter(DSLContext dsl, ProductStockMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductStockAggregateRoot save(ProductStockAggregateRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_STOCK)
                .set(record)
                .onConflict(PRODUCT_STOCK.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
