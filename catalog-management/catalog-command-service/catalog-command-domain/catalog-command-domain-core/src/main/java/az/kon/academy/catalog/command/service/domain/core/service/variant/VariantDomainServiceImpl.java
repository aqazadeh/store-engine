package az.kon.academy.catalog.command.service.domain.core.service.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.command.variant.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.VariantKeyQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.VariantValueQueryPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class VariantDomainServiceImpl implements VariantDomainService {

    @Override
    public VariantKeyRoot createKey(SeDomainContext context, VariantKeyCreateCommand command) {
        return VariantKeyRoot.initialize(command);
    }

    @Override
    public VariantKeyRoot changeKeyName(SeDomainContext context, VariantKeyChangeNameCommand command) {
        var variantKeyQueryPort = context.getQueryPort(VariantKeyQueryPort.class);
        var key = variantKeyQueryPort.fetchById(command.getVariantKeyId());
        return key.changeName(command);
    }

    @Override
    public VariantKeyRoot changeKeyDescription(SeDomainContext context, VariantKeyChangeDescriptionCommand command) {
        var variantKeyQueryPort = context.getQueryPort(VariantKeyQueryPort.class);
        var key = variantKeyQueryPort.fetchById(command.getVariantKeyId());
        return key.changeDescription(command);
    }

    @Override
    public VariantValueRoot createValue(SeDomainContext context, VariantValueCreateCommand command) {
        var variantKeyQueryPort = context.getQueryPort(VariantKeyQueryPort.class);
        variantKeyQueryPort.fetchById(command.getKeyId());
        return VariantValueRoot.initialize(command);
    }

    @Override
    public VariantValueRoot changeValueName(SeDomainContext context, VariantValueChangeNameCommand command) {
        var variantValueQueryPort = context.getQueryPort(VariantValueQueryPort.class);
        var value = variantValueQueryPort.fetchById(command.getVariantValueId());
        return value.changeName(command);
    }
}