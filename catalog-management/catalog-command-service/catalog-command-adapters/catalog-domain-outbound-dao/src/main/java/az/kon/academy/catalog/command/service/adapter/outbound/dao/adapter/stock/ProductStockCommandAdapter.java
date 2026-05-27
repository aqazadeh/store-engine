package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.stock;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductStockMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductStockCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_STOCK;

@Component
@RequiredArgsConstructor
public class ProductStockCommandAdapter implements ProductStockCommandPort {

    private final DSLContext dsl;
    private final ProductStockMapper mapper;

    @Override
    public ProductStockRoot save(ProductStockRoot aggregate) {
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
