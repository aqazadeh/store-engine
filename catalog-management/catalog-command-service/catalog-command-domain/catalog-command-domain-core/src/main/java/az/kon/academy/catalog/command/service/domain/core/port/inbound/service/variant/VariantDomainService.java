package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.command.variant.*;
import az.kon.academy.domain.core.SeDomainContext;

public interface VariantDomainService {
    VariantKeyRoot createKey(SeDomainContext context, VariantKeyCreateCommand command);
    VariantKeyRoot changeKeyName(SeDomainContext context, VariantKeyChangeNameCommand command);
    VariantKeyRoot changeKeyDescription(SeDomainContext context, VariantKeyChangeDescriptionCommand command);
    VariantValueRoot createValue(SeDomainContext context, VariantValueCreateCommand command);
    VariantValueRoot changeValueName(SeDomainContext context, VariantValueChangeNameCommand command);
}
