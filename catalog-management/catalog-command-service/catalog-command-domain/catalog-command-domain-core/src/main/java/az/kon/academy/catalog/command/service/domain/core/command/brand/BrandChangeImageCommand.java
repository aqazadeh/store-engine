package az.kon.academy.catalog.command.service.domain.core.command.brand;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandChangeImageCommand {
    @Getter private final BrandId brandId;
    @Getter private final String image;
}
