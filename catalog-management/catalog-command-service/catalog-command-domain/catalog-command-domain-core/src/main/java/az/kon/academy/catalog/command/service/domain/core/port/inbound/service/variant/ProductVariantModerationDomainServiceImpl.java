package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.variant;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantKeyRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.VariantValueRoot;
import az.kon.academy.catalog.command.service.domain.core.command.variant.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantKeyQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductVariantValueQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductVariantModerationDomainServiceImpl implements ProductVariantModerationDomainService {

    @Override
    public VariantKeyRoot createKey(SeDomainContext context, VariantKeyCreateCommand command) {
        return VariantKeyRoot.initialize(command);
    }

    @Override
    public VariantKeyRoot changeKeyName(SeDomainContext context, VariantKeyChangeNameCommand command) {
        var variantKeyQueryPort = context.getQueryPort(ProductVariantKeyQueryOutboundPort.class);
        var variantKey = variantKeyQueryPort.fetchById(command.getVariantKeyId());
        return variantKey.changeName(command);
    }

    @Override
    public VariantKeyRoot changeKeyDescription(SeDomainContext context, VariantKeyChangeDescriptionCommand command) {
        var variantKeyQueryPort = context.getQueryPort(ProductVariantKeyQueryOutboundPort.class);
        var variantKey = variantKeyQueryPort.fetchById(command.getVariantKeyId());
        return variantKey.changeDescription(command);
    }

    @Override
    public VariantValueRoot createValue(SeDomainContext context, VariantValueCreateCommand command) {
        var variantKeyQueryPort = context.getQueryPort(ProductVariantKeyQueryOutboundPort.class);
        variantKeyQueryPort.fetchById(command.getKeyId());
        return VariantValueRoot.initialize(command);
    }

    @Override
    public VariantValueRoot changeValueName(SeDomainContext context, VariantValueChangeNameCommand command) {
        var variantValueQueryPort = context.getQueryPort(ProductVariantValueQueryOutboundPort.class);
        var variantValue = variantValueQueryPort.fetchById(command.getVariantValueId());
        return variantValue.changeName(command);
    }
}