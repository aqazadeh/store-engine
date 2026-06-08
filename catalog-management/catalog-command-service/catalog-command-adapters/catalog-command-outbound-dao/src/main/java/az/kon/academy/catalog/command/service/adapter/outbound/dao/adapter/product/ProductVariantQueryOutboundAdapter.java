package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductVariantMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductVariantRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductVariantId;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantAssignmentRecord;
import az.kon.academy.catalog.sql.dal.tables.records.ProductVariantRecord;
import org.jooq.DSLContext;

import java.util.List;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_VARIANT_ASSIGNMENT;

@QueryAdapter
public class ProductVariantQueryOutboundAdapter implements ProductVariantQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductVariantMapper mapper;

    public ProductVariantQueryOutboundAdapter(DSLContext dsl, ProductVariantMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductVariantRoot fetchByIdAndProductIdAndMerchantId(ProductVariantId variantId, ProductId productId, MerchantId merchantId) {
        var variantRecord = dsl.select(PRODUCT_VARIANT.fields())
                .from(PRODUCT_VARIANT)
                .join(PRODUCT).on(PRODUCT_VARIANT.PRODUCT_ID.eq(PRODUCT.ID))
                .where(PRODUCT_VARIANT.ID.eq(variantId.value())
                        .and(PRODUCT_VARIANT.PRODUCT_ID.eq(productId.value()))
                        .and(PRODUCT.MERCHANT_ID.eq(merchantId.value())))
                .fetchOneInto(PRODUCT_VARIANT);

        if (variantRecord == null) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(variantId.toString()));
        }

        var assignments = dsl.selectFrom(PRODUCT_VARIANT_ASSIGNMENT)
                .where(PRODUCT_VARIANT_ASSIGNMENT.VARIANT_ID.eq(variantId.value()))
                .fetch();

        return mapper.toDomain(variantRecord, assignments);
    }

    @Override
    public void checkExistsByIdAndProductIdAndMerchantId(ProductVariantId variantId, ProductId productId, MerchantId merchantId) {
        if (!dsl.fetchExists(
                dsl.selectOne()
                        .from(PRODUCT_VARIANT)
                        .join(PRODUCT).on(PRODUCT_VARIANT.PRODUCT_ID.eq(PRODUCT.ID))
                        .where(PRODUCT_VARIANT.ID.eq(variantId.value())
                                .and(PRODUCT_VARIANT.PRODUCT_ID.eq(productId.value()))
                                .and(PRODUCT.MERCHANT_ID.eq(merchantId.value())))
        )) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(variantId.toString()));
        }
    }
}
