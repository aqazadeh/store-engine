package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductModerationDomainService extends ProductDomainService permits ProductModerationDomainServiceImpl {
    ProductRoot approve(SeDomainContext context, ProductApproveCommand command);
    ProductRoot reject(SeDomainContext context, ProductRejectCommand command);
    ProductRoot moveToInReview(SeDomainContext context, ProductMoveToInReviewCommand command);
    ProductRoot archive(SeDomainContext context, ProductArchiveCommand command);
}
