package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.product;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.ProductRejectionReasonMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.jooq.DSLContext;

import java.util.List;

import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT;
import static az.kon.academy.catalog.sql.dal.Tables.PRODUCT_REJECTION_REASON;

@QueryAdapter
public class ProductRejectionReasonQueryOutboundAdapter implements ProductRejectionReasonQueryOutboundPort {

    private final DSLContext dsl;
    private final ProductRejectionReasonMapper mapper;

    public ProductRejectionReasonQueryOutboundAdapter(DSLContext dsl, ProductRejectionReasonMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public ProductRejectionReasonRoot fetchById(ProductRejectionReasonId id) {
        return dsl.selectFrom(PRODUCT_REJECTION_REASON)
                .where(PRODUCT_REJECTION_REASON.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain)
                .orElseThrow(() -> new ProductEntityNotFoundException(
                        ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString())));
    }

    @Override
    public void checkExistsByIdAndMerchantId(ProductRejectionReasonId id, MerchantId merchantId) {
        if (!dsl.fetchExists(
                dsl.selectOne()
                        .from(PRODUCT_REJECTION_REASON)
                        .join(PRODUCT).on(PRODUCT_REJECTION_REASON.PRODUCT_ID.eq(PRODUCT.ID))
                        .where(PRODUCT_REJECTION_REASON.ID.eq(id.value())
                                .and(PRODUCT.MERCHANT_ID.eq(merchantId.value())))
        )) {
            throw new ProductEntityNotFoundException(ProductDomainErrorCodes.ENTITY_NOT_FOUND, List.of(id.toString()));
        }
    }
}
