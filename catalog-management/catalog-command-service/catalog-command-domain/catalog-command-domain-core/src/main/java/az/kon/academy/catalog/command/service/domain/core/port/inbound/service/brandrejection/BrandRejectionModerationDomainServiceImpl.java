package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class BrandRejectionModerationDomainServiceImpl implements BrandRejectionModerationDomainService{

    @Override
    public BrandRejectionReasonRoot add(SeDomainContext context, BrandRejectionReasonAddCommand command) {
        return BrandRejectionReasonRoot.initialize(command);
    }

    @Override
    public BrandRejectionReasonRoot remove(SeDomainContext context, BrandRejectionReasonRemoveCommand command) {
        final var brandRejectionReasonQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var brandRejectionReason = brandRejectionReasonQuery.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.remove(command);
    }

    @Override
    public BrandRejectionReasonRoot changeReason(SeDomainContext context, BrandRejectionReasonChangeReasonCommand command) {
        final var brandRejectionReasonQueryPort = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var brandRejectionReason = brandRejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.changeReason(command);
    }
}
