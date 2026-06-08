package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonSolveCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductRejectionManagementDomainService extends ProductRejectionDomainService permits
        ProductRejectionManagementDomainServiceImpl {

    ProductRejectionReasonRoot solve(SeDomainContext context, ProductRejectionReasonSolveCommand command);

}
