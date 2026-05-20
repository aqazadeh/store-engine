package az.kon.academy.catalog.command.service.domain.core.command.variant;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class VariantKeyChangeDescriptionCommand {
    @Getter private final VariantKeyId variantKeyId;
    @Getter private final String description;
}