package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonSolveCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface BrandRejectionManagementService extends BrandRejectionDomainService permits BrandRejectionManagementServiceImpl {

    BrandRejectionReasonRoot solve(SeDomainContext context, BrandRejectionReasonSolveCommand command);

}
