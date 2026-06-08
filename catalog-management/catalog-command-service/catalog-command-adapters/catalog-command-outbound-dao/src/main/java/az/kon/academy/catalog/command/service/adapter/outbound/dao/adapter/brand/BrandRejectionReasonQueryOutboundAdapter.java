package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandRejectionReasonMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandRejectionDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandRejectionDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import org.jooq.DSLContext;

import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.BRAND_REJECTION_REASON;

@QueryAdapter
public class BrandRejectionReasonQueryOutboundAdapter implements BrandRejectionReasonQueryOutboundPort {

    private final DSLContext dsl;
    private final BrandRejectionReasonMapper mapper;

    public BrandRejectionReasonQueryOutboundAdapter(DSLContext dsl, BrandRejectionReasonMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<BrandRejectionReasonRoot> findByIdRowStatusActive(BrandRejectionReasonId id) {
        return dsl.selectFrom(BRAND_REJECTION_REASON)
                .where(BRAND_REJECTION_REASON.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public BrandRejectionReasonRoot fetchByIdAndRowStatusActive(BrandRejectionReasonId id) {
        return this.findByIdRowStatusActive(id)
                .orElseThrow(() -> new BrandRejectionDomainException(BrandRejectionDomainErrorCodes.ENTITY_NOT_FOUND));
    }

    @Override
    public boolean existsByBrandIdAndNotSolved(BrandId brandId) {
        return dsl.fetchExists(
                BRAND_REJECTION_REASON,
                BRAND_REJECTION_REASON.BRAND_ID.eq(brandId.value())
                        .and(BRAND_REJECTION_REASON.SOLVED.eq(Boolean.FALSE))
                        .and(BRAND_REJECTION_REASON.ROW_STATUS.eq(RowStatusType.ACTIVE))
        );
    }
}
