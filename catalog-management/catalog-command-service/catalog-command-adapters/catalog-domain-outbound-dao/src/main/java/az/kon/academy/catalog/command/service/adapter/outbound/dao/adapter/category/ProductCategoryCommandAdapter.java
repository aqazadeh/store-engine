package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_CATEGORY;

@CommandAdapter
public class ProductCategoryCommandAdapter implements ProductCategoryCommandPort {

    private final DSLContext dsl;
    private final ProductCategoryMapper mapper;

    public ProductCategoryCommandAdapter(DSLContext dsl, ProductCategoryMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductCategoryRoot save(ProductCategoryRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_CATEGORY)
                .set(record)
                .onConflict(PRODUCT_CATEGORY.ID)
                .doUpdate()
                .set(record)
                .execute();
        return aggregate;
    }
}
