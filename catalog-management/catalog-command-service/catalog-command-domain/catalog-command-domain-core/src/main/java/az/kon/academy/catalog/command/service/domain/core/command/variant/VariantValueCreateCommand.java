package az.kon.academy.catalog.command.service.domain.core.command.variant;

import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import lombok.Builder;
import lombok.Getter;

@Builder
public final class VariantValueCreateCommand {
    @Getter private final VariantKeyId keyId;
    @Getter private final VariantValue name;
}