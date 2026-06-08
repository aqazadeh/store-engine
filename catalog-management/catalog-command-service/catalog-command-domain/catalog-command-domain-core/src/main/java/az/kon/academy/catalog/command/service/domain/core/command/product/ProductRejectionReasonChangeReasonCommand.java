package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.management.ProductRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductRejectionReasonChangeReasonCommand {
    @Getter private final ProductRejectionReasonId rejectionReasonId;
    @Getter private final ModeratorId moderatorId;
    @Getter private final String reason;
}
