package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductMapper;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductVariantMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantAssignmentRecord;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantRecord;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_SPECIFICATION_ASSIGNMENT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT_ASSIGNMENT;
import static java.util.stream.Collectors.groupingBy;

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
    public Optional<ProductRoot> findById(ProductId id) {
        var productRecord = dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id.value()))
                .fetchOne();
        if (productRecord == null) {
            return Optional.empty();
        }

        var specAssignments = dsl.selectFrom(PRODUCT_SPECIFICATION_ASSIGNMENT)
                .where(PRODUCT_SPECIFICATION_ASSIGNMENT.PRODUCT_ID.eq(id.value()))
                .fetch();

        var variantRecords = dsl.selectFrom(PRODUCT_VARIANT)
                .where(PRODUCT_VARIANT.PRODUCT_ID.eq(id.value()))
                .fetch();

        List<ProductVariantRoot> variants = variantRecords.isEmpty()
                ? List.of()
                : buildVariants(variantRecords);

        return Optional.of(mapper.toDomain(productRecord, specAssignments, variants));
    }

    private List<ProductVariantRoot> buildVariants(List<ProductVariantRecord> variantRecords) {
        var variantIds = variantRecords.stream()
                .map(ProductVariantRecord::getId)
                .toList();

        Map<UUID, List<ProductVariantAssignmentRecord>> assignmentsByVariant = variantIds.isEmpty()
                ? Map.of()
                : dsl.selectFrom(PRODUCT_VARIANT_ASSIGNMENT)
                        .where(PRODUCT_VARIANT_ASSIGNMENT.VARIANT_ID.in(variantIds))
                        .fetch()
                        .stream()
                        .collect(groupingBy(ProductVariantAssignmentRecord::getVariantId));

        return variantRecords.stream()
                .map(vr -> variantMapper.toDomain(vr,
                        assignmentsByVariant.getOrDefault(vr.getId(), List.of())))
                .toList();
    }

    @Override
    public ProductRoot fetchById(ProductId id) {
        return this.findById(id).orElseThrow(() ->
                new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString())));
    }

    @Override
    public ProductRoot fetchByIdAndMerchantId(ProductId id, MerchantId merchantId) {
        var productRecord = dsl.selectFrom(PRODUCT)
                .where(PRODUCT.ID.eq(id.value())
                        .and(PRODUCT.MERCHANT_ID.eq(merchantId.value())))
                .fetchOne();
        if (productRecord == null) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString()));
        }

        var specAssignments = dsl.selectFrom(PRODUCT_SPECIFICATION_ASSIGNMENT)
                .where(PRODUCT_SPECIFICATION_ASSIGNMENT.PRODUCT_ID.eq(id.value()))
                .fetch();

        var variantRecords = dsl.selectFrom(PRODUCT_VARIANT)
                .where(PRODUCT_VARIANT.PRODUCT_ID.eq(id.value()))
                .fetch();

        List<ProductVariantRoot> variants = variantRecords.isEmpty()
                ? List.of()
                : buildVariants(variantRecords);

        return mapper.toDomain(productRecord, specAssignments, variants);
    }

    @Override
    public void checkExistsByIdAndMerchantId(ProductId id, MerchantId merchantId) {
        if (!dsl.fetchExists(
                PRODUCT,
                PRODUCT.ID.eq(id.value())
                        .and(PRODUCT.MERCHANT_ID.eq(merchantId.value()))
        )) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString()));
        }
    }

    @Override
    public void checkExistsById(ProductId id) {
        if (!dsl.fetchExists(
                PRODUCT,
                PRODUCT.ID.eq(id.value())
        )) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString()));
        }
    }

    @Override
    public Boolean exitsByCategoryId(ProductCategoryId productCategoryId) {
        return dsl.fetchExists(
                PRODUCT,
                PRODUCT.CATEGORY_ID.eq(productCategoryId.value())
        );
    }
}
