package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandRejectionReasonQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class BrandRejectionManagementServiceImpl implements BrandRejectionManagementService {

    @Override
    public BrandRejectionReasonRoot solve(SeDomainContext context, BrandRejectionReasonSolveCommand command) {
        final var brandRejectionReasonQuery = context.getQueryPort(BrandRejectionReasonQueryOutboundPort.class);
        final var brandRejectionReason = brandRejectionReasonQuery.fetchByIdAndRowStatusActive(command.getBrandRejectionReasonId());
        return brandRejectionReason.markAsSolved();
    }

}
