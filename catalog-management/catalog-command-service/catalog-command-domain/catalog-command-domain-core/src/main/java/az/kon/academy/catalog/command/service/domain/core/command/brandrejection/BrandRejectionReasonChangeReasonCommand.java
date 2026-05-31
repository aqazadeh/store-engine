package az.kon.academy.catalog.command.service.domain.core.command.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandRejectionReasonId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandRejectionReasonChangeReasonCommand {
    @Getter private final BrandRejectionReasonId brandRejectionReasonId;
    @Getter private final ModeratorId moderatorId;
    @Getter private final String reason;
}
