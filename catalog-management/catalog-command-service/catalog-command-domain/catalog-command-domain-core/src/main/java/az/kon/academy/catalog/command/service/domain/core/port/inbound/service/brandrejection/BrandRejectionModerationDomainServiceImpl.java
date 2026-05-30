package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class BrandRejectionModerationDomainServiceImpl implements BrandRejectionModerationDomainService{

    @Override
    public BrandRejectionReasonRoot add(SeDomainContext context, BrandRejectionReasonAddCommand command) {
        return BrandRejectionReasonRoot.initialize(command);
    }

    @Override
    public BrandRejectionReasonRoot remove(SeDomainContext context, BrandRejectionReasonRemoveCommand command) {
        var brandRejectionReasonQueryPort = context.getQueryPort(BrandRejectionReasonQueryPort.class);
        var brandRejectionReason = brandRejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.remove(command);
    }

    @Override
    public BrandRejectionReasonRoot changeReason(SeDomainContext context, BrandRejectionReasonChangeReasonCommand command) {
        var brandRejectionReasonQueryPort = context.getQueryPort(BrandRejectionReasonQueryPort.class);
        var brandRejectionReason = brandRejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.changeReason(command);
    }
}
