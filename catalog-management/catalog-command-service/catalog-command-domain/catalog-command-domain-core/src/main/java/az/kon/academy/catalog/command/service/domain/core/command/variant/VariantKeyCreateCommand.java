package az.kon.academy.catalog.command.service.domain.core.command.variant;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class VariantKeyCreateCommand {
    @Getter private final VariantName name;
    @Getter private final String description;
}