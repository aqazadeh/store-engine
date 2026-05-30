package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.CommandAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductMapper;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductVariantMapper;
import az.kon.academy.catalog.command.service.application.service.port.outbound.ProductCommandOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import org.jooq.DSLContext;

import java.util.List;
import java.util.UUID;

import static az.kon.academy.catalog.command.dal.Tables.*;

@CommandAdapter
public class ProductCommandOutboundAdapter implements ProductCommandOutboundPort {

    private final DSLContext dsl;
    private final ProductMapper mapper;
    private final ProductVariantMapper variantMapper;

    public ProductCommandOutboundAdapter(DSLContext dsl, ProductMapper mapper, ProductVariantMapper variantMapper) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.variantMapper = variantMapper;
    }

    @Override
    public ProductRoot save(ProductRoot aggregate) {
        UUID productId = aggregate.getRootID().value();

        var record = mapper.toRecord(aggregate);
        dsl.insertInto(PRODUCT)
                .set(record)
                .onConflict(PRODUCT.ID)
                .doUpdate()
                .set(record)
                .execute();

        dsl.deleteFrom(PRODUCT_SPECIFICATION_ASSIGNMENT)
                .where(PRODUCT_SPECIFICATION_ASSIGNMENT.PRODUCT_ID.eq(productId))
                .execute();

        if (!aggregate.getSpecifications().isEmpty()) {
            var specRecords = aggregate.getSpecifications().stream()
                    .map(s -> mapper.toSpecAssignmentRecord(aggregate, s))
                    .toList();
            dsl.batchInsert(specRecords).execute();
        }

        dsl.deleteFrom(PRODUCT_VARIANT_ASSIGNMENT)
                .where(PRODUCT_VARIANT_ASSIGNMENT.VARIANT_ID.in(
                        dsl.select(PRODUCT_VARIANT.ID)
                                .from(PRODUCT_VARIANT)
                                .where(PRODUCT_VARIANT.PRODUCT_ID.eq(productId))
                ))
                .execute();

        dsl.deleteFrom(PRODUCT_VARIANT)
                .where(PRODUCT_VARIANT.PRODUCT_ID.eq(productId))
                .execute();

        if (!aggregate.getVariants().isEmpty()) {
            var variantRecords = aggregate.getVariants().stream()
                    .map(v -> variantMapper.toRecord(v, productId))
                    .toList();
            dsl.batchInsert(variantRecords).execute();

            List<az.kon.academy.catalog.command.dal.tables.records.ProductVariantAssignmentRecord> assignmentRecords =
                    aggregate.getVariants().stream()
                            .flatMap(v -> v.getAssignments().stream()
                                    .map(a -> variantMapper.toAssignmentRecord(v.getRootID().value(), a)))
                            .toList();

            if (!assignmentRecords.isEmpty()) {
                dsl.batchInsert(assignmentRecords).execute();
            }
        }

        return aggregate;
    }
}
