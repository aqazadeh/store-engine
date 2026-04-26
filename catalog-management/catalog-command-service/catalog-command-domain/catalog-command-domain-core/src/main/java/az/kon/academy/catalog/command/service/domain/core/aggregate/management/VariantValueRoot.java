package az.kon.academy.catalog.command.service.domain.core.aggregate.management;

import az.kon.academy.aggragate.AggregateRoot;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantKeyId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValue;
import az.kon.academy.catalog.command.service.domain.core.vo.management.variant.VariantValueId;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class VariantValueRoot extends AggregateRoot<VariantValueRoot, VariantValueId> {
    private VariantKeyId keyId;
    private VariantValue name;
}
