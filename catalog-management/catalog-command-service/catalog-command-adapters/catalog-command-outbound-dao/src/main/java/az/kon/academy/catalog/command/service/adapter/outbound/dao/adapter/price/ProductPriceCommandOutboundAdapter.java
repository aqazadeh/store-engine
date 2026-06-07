package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductPriceMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductPriceCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceAggregateRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_PRICE;

@CommandAdapter
public class ProductPriceCommandOutboundAdapter implements ProductPriceCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductPriceMapper mapper;

    public ProductPriceCommandOutboundAdapter(DSLContext dsl, ProductPriceMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductPriceAggregateRoot save(ProductPriceAggregateRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_PRICE)
                .set(record)
                .onConflict(PRODUCT_PRICE.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
