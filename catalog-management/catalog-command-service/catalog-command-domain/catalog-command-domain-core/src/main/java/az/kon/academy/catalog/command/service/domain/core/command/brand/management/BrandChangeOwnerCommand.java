package az.kon.academy.catalog.command.service.domain.core.command.brand.management;

import az.kon.academy.catalog.command.service.domain.core.vo.brand.BrandId;
import az.kon.academy.catalog.command.service.domain.core.vo.merchent.MerchantId;
import lombok.Builder;
import lombok.Getter;

@Builder
public class BrandChangeOwnerCommand {
    @Getter private final BrandId brandId;
    @Getter private final MerchantId owner;
}
