package az.kon.academy.catalog.command.service.domain.core.command.brand.management;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandRejectCommand {
    @Getter private final BrandId brandId;
}
