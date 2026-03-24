package az.kon.academy.catalog.command.service.domain.core.command.brand;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandPath;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandChangeInformationCommand {
    @Getter private final BrandId brandId;
    @Getter private final BrandName name;
    @Getter private final BrandDescription description;
    @Getter private final BrandPath path;
}
