package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class VariantKeyRoot extends AggregateRoot<VariantKeyRoot, VariantKeyId> {
    private VariantName name;
    private String description;
}
