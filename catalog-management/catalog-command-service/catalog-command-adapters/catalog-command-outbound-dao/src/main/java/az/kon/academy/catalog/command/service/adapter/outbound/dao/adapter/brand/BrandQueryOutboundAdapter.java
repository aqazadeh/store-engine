package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.application.core.annotation.QueryAdapter;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryEntityNotFoundException;
import az.kon.academy.catalog.sql.dal.enums.RowStatusType;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import org.jooq.DSLContext;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.sql.dal.Tables.BRAND;

@QueryAdapter
public class BrandQueryOutboundAdapter implements BrandQueryOutboundPort {

    private final DSLContext dsl;
    private final BrandMapper mapper;

    public BrandQueryOutboundAdapter(DSLContext dsl, BrandMapper mapper) {
        this.dsl = dsl;
        this.mapper = mapper;
    }

    @Override
    public Optional<BrandRoot> findById(final BrandId brandId) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(brandId.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public BrandRoot fetchById(final BrandId brandId) {
        return this.findById(brandId)
                .orElseThrow(() -> new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND));
    }

    @Override
    public Optional<BrandRoot> findByIdAndMerchantId(final BrandId brandId, final MerchantId merchantId) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(brandId.value())
                        .and(BRAND.OWNER_ID.eq(merchantId.value()))
                        .and(BRAND.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public BrandRoot fetchByIdAndMerchantId(BrandId brandId, MerchantId merchantId) {
        return this.findByIdAndMerchantId(brandId, merchantId)
                .orElseThrow(() -> new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND));
    }

    @Override
    public Optional<BrandRoot> findByIdAndIsGlobalTrue(BrandId brandId) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(brandId.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatusType.ACTIVE))
                        .and(BRAND.IS_GLOBAL.eq(Boolean.TRUE)))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public BrandRoot fetchByIdAndIsGlobalTrue(BrandId brandId) {
        return this.findByIdAndIsGlobalTrue(brandId)
                .orElseThrow(() -> new BrandDomainException(BrandDomainErrorCodes.ENTITY_NOT_FOUND));
    }

    @Override
    public Integer fetchCountByMerchantId(final MerchantId merchantId) {
        return dsl.selectCount()
                .from(BRAND)
                .where(BRAND.OWNER_ID.eq(merchantId.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatusType.ACTIVE)))
                .fetchOne(0, Integer.class);
    }

    @Override
    public void checkExistsByName(final BrandName name) {
        if(!dsl.fetchExists(
                BRAND,
                BRAND.NAME.eq(name.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatusType.ACTIVE))
        )){
            throw new ProductCategoryEntityNotFoundException(
                    BrandDomainErrorCodes.NAME_ALREADY_EXISTS,
                    List.of(name.toString())
            );
        }
    }

    @Override
    public void checkExitsById(BrandId brandId) {

    }
}
