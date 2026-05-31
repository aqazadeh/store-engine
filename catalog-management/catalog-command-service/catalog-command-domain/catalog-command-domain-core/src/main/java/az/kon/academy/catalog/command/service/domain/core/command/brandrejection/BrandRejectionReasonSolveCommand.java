package az.kon.academy.catalog.command.service.domain.core.command.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandRejectionReasonSolveCommand {
    @Getter private final BrandRejectionReasonId brandRejectionReasonId;
    @Getter private final MerchantId merchantId;
}
