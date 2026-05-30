package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class BrandRejectionManagementServiceImpl implements BrandRejectionManagementService {

    @Override
    public BrandRejectionReasonRoot solve(SeDomainContext context, BrandRejectionReasonSolveCommand command) {
        var brandRejectionReasonQueryPort = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        var brandRejectionReason = brandRejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.markAsSolved();
    }

}
