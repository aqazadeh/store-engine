package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.category;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductCategoryMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCategoryCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.dao.OptimisticLockingFailureException;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_CATEGORY;

@Slf4j
@CommandAdapter
public class ProductCategoryCommandOutboundAdapter implements ProductCategoryCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductCategoryMapper mapper;

    public ProductCategoryCommandOutboundAdapter(DSLContext dsl, ProductCategoryMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductCategoryRoot save(ProductCategoryRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        var result = dsl.insertInto(PRODUCT_CATEGORY)
                .set(record)
                .onConflict(PRODUCT_CATEGORY.ID)
                .doUpdate()
                .set(record)
                .where(PRODUCT_CATEGORY.VERSION.eq(aggregate.getVersion().value()))
                .execute();

        if(result == 0){
            log.error("Concurrent update detected for ProductCategory id={}", aggregate.getRootID());
            throw new OptimisticLockingFailureException("Concurrent update detected for ProductCategory id=" + aggregate.getRootID());
        }
        return this.mapper.toDomain(record);
    }
}
