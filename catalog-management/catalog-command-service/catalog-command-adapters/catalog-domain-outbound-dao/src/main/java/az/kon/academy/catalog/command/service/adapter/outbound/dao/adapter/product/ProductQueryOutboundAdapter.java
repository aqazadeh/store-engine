package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.dal.tables.records.ProductVariantRecord;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductMapper;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductVariantMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static az.kon.academy.catalog.command.dal.Tables.*;

@QueryAdapter
public class ProductQueryOutboundAdapter implements ProductQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductMapper mapper;
    private final ProductVariantMapper variantMapper;

    public ProductQueryOutboundAdapter(DSLContext dsl, ProductMapper mapper, ProductVariantMapper variantMapper) {
        this.dsl = dsl;
        this.mapper = mapper;
        this.variantMapper = variantMapper;
    }

    @Override
    public Optional<ProductRoot> findByIdAndRowStatusActive(ProductId id) {
        return dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id.value())
                        .and(PRODUCT.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(productRecord -> {
                    UUID productId = productRecord.getId();

                    var specAssignments = dsl.selectFrom(PRODUCT_SPECIFICATION_ASSIGNMENT)
                            .where(PRODUCT_SPECIFICATION_ASSIGNMENT.PRODUCT_ID.eq(productId))
                            .fetch();

                    var variantRecords = dsl.selectFrom(PRODUCT_VARIANT)
                            .where(PRODUCT_VARIANT.PRODUCT_ID.eq(productId))
                            .fetch();

                    List<UUID> variantIds = variantRecords.stream()
                            .map(ProductVariantRecord::getId)
                            .toList();

                    Map<UUID, List<az.kon.academy.catalog.command.dal.tables.records.ProductVariantAssignmentRecord>> assignmentsByVariant =
                            variantIds.isEmpty()
                                    ? Map.of()
                                    : dsl.selectFrom(PRODUCT_VARIANT_ASSIGNMENT)
                                    .where(PRODUCT_VARIANT_ASSIGNMENT.VARIANT_ID.in(variantIds))
                                    .fetch()
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                            az.kon.academy.catalog.command.dal.tables.records.ProductVariantAssignmentRecord::getVariantId
                                    ));

                    List<ProductVariantRoot> variants = variantRecords.stream()
                            .map(v -> variantMapper.toDomain(
                                    v,
                                    assignmentsByVariant.getOrDefault(v.getId(), List.of())
                            ))
                            .toList();

                    return mapper.toDomain(productRecord, specAssignments, variants);
                });
    }
}
