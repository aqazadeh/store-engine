package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductSpecificationMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductSpecificationRoot;
import org.jooq.DSLContext;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_SPECIFICATION;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT;

@CommandAdapter
public class ProductSpecificationCommandOutboundAdapter implements ProductSpecificationCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductSpecificationMapper mapper;

    public ProductSpecificationCommandOutboundAdapter(DSLContext dsl, ProductSpecificationMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductSpecificationRoot save(ProductSpecificationRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_SPECIFICATION)
                .set(record)
                .onConflict(PRODUCT_SPECIFICATION.ID)
                .doUpdate()
                .set(record)
                .execute();

        var specId = aggregate.getRootID().value();// FIXME optimize add assignment new field deleted new and unchanged and filter it delete or update

        dsl.deleteFrom(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT)
                .where(PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT.SPECIFICATION_ID.eq(specId))
                .execute();

        if (!aggregate.getCategories().isEmpty()) {
            var assignmentRecords = aggregate.getCategories().stream()
                    .map(a -> mapper.toCategoryAssignmentRecord(specId, a))
                    .toList();
            dsl.batchInsert(assignmentRecords).execute();
        }

        return aggregate;
    }
}
