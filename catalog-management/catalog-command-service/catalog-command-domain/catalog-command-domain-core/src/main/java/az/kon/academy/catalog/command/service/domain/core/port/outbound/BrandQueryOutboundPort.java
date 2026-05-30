package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.Optional;

public interface BrandQueryOutboundPort extends BaseQueryPort {

    Optional<BrandRoot> findById(final BrandId brandId);
    BrandRoot fetchById(final BrandId brandId);

    Optional<BrandRoot> findByIdAndMerchantId(final BrandId brandId, final MerchantId merchantId);
    BrandRoot fetchByIdAndMerchantId(final BrandId brandId, final MerchantId merchantId);

    Optional<BrandRoot> findByIdAndIsGlobalTrue(final BrandId brandId);
    BrandRoot fetchByIdAndIsGlobalTrue(final BrandId brandId);

    Integer fetchCountByMerchantId(final MerchantId merchantId);
    Boolean existsByName(final BrandName name);
}
