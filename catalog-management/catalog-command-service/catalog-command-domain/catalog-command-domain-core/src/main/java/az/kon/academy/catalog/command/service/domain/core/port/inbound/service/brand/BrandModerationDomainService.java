package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface BrandModerationDomainService extends BrandDomainService permits BrandModerationDomainServiceImpl{

    BrandRoot changeOwner(SeDomainContext context, BrandChangeOwnerCommand command);

    BrandRoot approve(SeDomainContext context, BrandApproveCommand command);

    BrandRoot reject(SeDomainContext context, BrandRejectCommand command);

    BrandRoot changeToGlobal(SeDomainContext context, BrandChangeGlobalCommand command);

    BrandRoot moveToInReview(SeDomainContext context, BrandMoveToInReviewCommand command);
}
