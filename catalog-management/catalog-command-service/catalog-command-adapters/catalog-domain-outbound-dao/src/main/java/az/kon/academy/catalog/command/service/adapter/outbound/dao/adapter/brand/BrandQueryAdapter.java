package az.kon.academy.catalog.command.service.adapter.outbound.dao.adapter.brand;

import az.kon.academy.aggragate.valueobject.RowStatus;
import az.kon.academy.catalog.command.service.adapter.outbound.dao.mapper.BrandMapper;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static az.kon.academy.catalog.command.dal.Tables.BRAND;

@Component
@RequiredArgsConstructor
public class BrandQueryAdapter implements BrandQueryPort {

    private final DSLContext dsl;
    private final BrandMapper mapper;

    @Override
    public Optional<BrandRoot> fetchById(BrandId id) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(id.value()))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<BrandRoot> findByIdAndMerchantIdAndRowStatusActive(BrandId brandId, MerchantId merchantId) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(brandId.value())
                        .and(BRAND.OWNER_ID.eq(merchantId.value()))
                        .and(BRAND.ROW_STATUS.eq(RowStatus.ACTIVE.name())))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<BrandRoot> findByIdAndRowStatusActive(BrandId id) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.ID.eq(id.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatus.ACTIVE.name())))
                .fetchOptional()
                .map(mapper::toDomain);
    }

    @Override
    public List<BrandRoot> fetchAllByMerchantIdAndRowStatusActive(MerchantId merchantId) {
        return dsl.selectFrom(BRAND)
                .where(BRAND.OWNER_ID.eq(merchantId.value())
                        .and(BRAND.ROW_STATUS.eq(RowStatus.ACTIVE.name())))
                .fetch()
                .map(mapper::toDomain);
    }

    @Override
    public Boolean existsByNameAndRowStatusActive(BrandName name) {
        return dsl.fetchExists(
                dsl.selectFrom(BRAND)
                        .where(BRAND.NAME.eq(name.value())
                                .and(BRAND.ROW_STATUS.eq(RowStatus.ACTIVE.name())))
        );
    }
}
