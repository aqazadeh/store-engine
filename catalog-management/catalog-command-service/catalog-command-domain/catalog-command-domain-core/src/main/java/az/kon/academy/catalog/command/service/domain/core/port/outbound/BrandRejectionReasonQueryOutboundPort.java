package az.kon.academy.catalog.command.service.domain.core.port.outbound;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.domain.core.BaseQueryPort;

import java.util.Optional;

public interface BrandRejectionReasonQueryOutboundPort extends BaseQueryPort {

    Optional<BrandRejectionReasonRoot> findByIdRowStatusActive(BrandRejectionReasonId id);

    BrandRejectionReasonRoot fetchByIdAndRowStatusActive(BrandRejectionReasonId id);

    boolean existsByBrandIdAndNotSolved(BrandId brandId);
}
