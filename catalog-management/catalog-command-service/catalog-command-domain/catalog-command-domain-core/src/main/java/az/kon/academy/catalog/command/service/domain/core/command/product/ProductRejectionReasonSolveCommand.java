package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductRejectionReasonSolveCommand {
    @Getter private final ProductRejectionReasonId rejectionReasonId;
    @Getter private final MerchantId merchantId;
}
