package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.specification;

import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductSpecificationMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductSpecificationCommandPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;

import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_SPECIFICATION;
import static az.kon.academy.catalog.command.dal.Tables.PRODUCT_SPECIFICATION_CATEGORY_ASSIGNMENT;

@Component
@RequiredArgsConstructor
public class ProductSpecificationCommandAdapter implements ProductSpecificationCommandPort {

    private final DSLContext dsl;
    private final ProductSpecificationMapper mapper;

    @Override
    public ProductSpecificationRoot save(ProductSpecificationRoot aggregate) {
        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT_SPECIFICATION)
                .set(record)
                .onConflict(PRODUCT_SPECIFICATION.ID)
                .doUpdate()
                .set(record)
                .execute();

        var specId = aggregate.getRootID().value();

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
