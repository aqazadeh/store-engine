package az.kon.academy.catalog.command.service.domain.core.command.brand;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.moderation.ModeratorId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandCreateRejectionReasonCommand {
    @Getter private final BrandId brandId;
    @Getter private final String reason;
    @Getter private final ModeratorId moderatedBy;
}