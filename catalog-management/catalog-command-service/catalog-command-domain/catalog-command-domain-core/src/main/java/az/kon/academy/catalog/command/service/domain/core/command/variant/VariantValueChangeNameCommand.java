package az.kon.academy.catalog.command.service.domain.core.command.variant;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class VariantValueChangeNameCommand {
    @Getter private final VariantValueId variantValueId;
    @Getter private final VariantValue name;
}