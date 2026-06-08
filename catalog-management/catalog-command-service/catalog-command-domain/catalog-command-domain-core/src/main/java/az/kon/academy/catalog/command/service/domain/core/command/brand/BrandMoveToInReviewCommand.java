package az.kon.academy.catalog.command.service.domain.core.command.brand;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandMoveToInReviewCommand {
    @Getter private final BrandId brandId;
}
