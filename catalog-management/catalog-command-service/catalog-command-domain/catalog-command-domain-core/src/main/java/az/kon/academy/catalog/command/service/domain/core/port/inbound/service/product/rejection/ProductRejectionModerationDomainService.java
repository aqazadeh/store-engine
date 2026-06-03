package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.rejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.ProductCreateRejectionReasonCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductRejectionModerationDomainService extends ProductRejectionDomainService permits
        ProductRejectionModerationDomainServiceImpl {

    ProductRejectionReasonRoot createRejectionReason(SeDomainContext context, ProductCreateRejectionReasonCommand command);

}
