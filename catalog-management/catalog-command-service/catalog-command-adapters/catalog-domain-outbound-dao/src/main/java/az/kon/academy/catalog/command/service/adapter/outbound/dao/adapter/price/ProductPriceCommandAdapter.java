package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.price;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductPriceMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductPriceCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_PRICE;

@CommandAdapter
public class ProductPriceCommandAdapter implements ProductPriceCommandPort {

    private final DSLContext dsl;
    private final ProductPriceMapper mapper;

    public ProductPriceCommandAdapter(DSLContext dsl, ProductPriceMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductPriceRoot save(ProductPriceRoot aggregate) {
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
