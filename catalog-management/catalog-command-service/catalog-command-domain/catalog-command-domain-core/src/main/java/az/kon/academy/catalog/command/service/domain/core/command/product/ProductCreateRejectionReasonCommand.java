package az.kon.academy.catalog.command.service.domain.core.command.product;

import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class ProductCreateRejectionReasonCommand {
    @Getter private final ProductId productId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;
}