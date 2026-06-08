package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonSolveCommand;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductRejectionReasonQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductRejectionManagementDomainServiceImpl implements ProductRejectionManagementDomainService {

    @Override
    public ProductRejectionReasonRoot solve(SeDomainContext context, ProductRejectionReasonSolveCommand command) {
        final var rejectionReasonQuery = context.getQueryPort(ProductRejectionReasonQueryOutboundPort.class);
        rejectionReasonQuery.checkExistsByIdAndMerchantId(
                command.getRejectionReasonId(), command.getMerchantId());
        final var rejectionReason = rejectionReasonQuery.fetchByIdAndRowStatusActive(command.getRejectionReasonId());
        return rejectionReason.markAsSolved();
    }

}
