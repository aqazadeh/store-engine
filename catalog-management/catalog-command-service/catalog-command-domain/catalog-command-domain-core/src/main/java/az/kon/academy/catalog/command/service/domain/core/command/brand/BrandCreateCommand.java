package az.kon.academy.catalog.command.service.domain.core.command.brand;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandName;
import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandPath;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class BrandCreateCommand {
    @Getter private final MerchantId owner;
    @Getter private final BrandName name;
    @Getter private final BrandDescription description;
    @Getter private final BrandPath path;
}
