package az.kon.academy.catalog.command.service.domain.core.command.variant;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class VariantKeyChangeNameCommand {
    @Getter private final VariantKeyId variantKeyId;
    @Getter private final VariantName name;
}