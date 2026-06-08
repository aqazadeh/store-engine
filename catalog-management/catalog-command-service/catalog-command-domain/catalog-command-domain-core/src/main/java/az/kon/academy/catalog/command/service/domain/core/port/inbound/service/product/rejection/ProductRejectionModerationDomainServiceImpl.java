package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductRejectionReasonRemoveCommand;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductRejectionReasonQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductId;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class ProductRejectionModerationDomainServiceImpl implements ProductRejectionModerationDomainService {

    @Override
    public ProductRejectionReasonRoot createRejectionReason(SeDomainContext context, ProductCreateRejectionReasonCommand command) {
        checkProductStatusForRejection(context, command.getProductId());
        return ProductRejectionReasonRoot.initialize(command);
    }

    @Override
    public ProductRejectionReasonRoot changeReason(SeDomainContext context, ProductRejectionReasonChangeReasonCommand command) {
        final var rejectionReasonQueryPort = context.getQueryPort(ProductRejectionReasonQueryOutboundPort.class);
        final var rejectionReason = rejectionReasonQueryPort.fetchByIdAndRowStatusActive(command.getRejectionReasonId());
        checkProductStatusForRejection(context, rejectionReason.getProductId());
        return rejectionReason.changeReason(command);
    }

    @Override
    public ProductRejectionReasonRoot remove(SeDomainContext context, ProductRejectionReasonRemoveCommand command) {
        final var rejectionReasonQuery = context.getQueryPort(ProductRejectionReasonQueryOutboundPort.class);
        final var rejectionReason = rejectionReasonQuery.fetchByIdAndRowStatusActive(command.getRejectionReasonId());
        checkProductStatusForRejection(context, rejectionReason.getProductId());
        return rejectionReason.remove(command);
    }

    private void checkProductStatusForRejection(SeDomainContext context, ProductId productId) {
        final var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        final var product = productQuery.fetchById(productId);
        if (!product.getStatus().isInReview() && !product.getStatus().isSentToApproval()) {
            throw new ProductDomainException(
                    ProductDomainErrorCodes.STATUS_INVALID_FOR_REJECTION_OPERATION,
                    List.of(productId.toString())
            );
        }
    }
}
