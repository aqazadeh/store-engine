package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandMoveToDraftCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandSentToApprovalCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface BrandManagementDomainService  extends BrandDomainService permits BrandManagementDomainServiceImpl{

    BrandRoot sentToApproval(final SeDomainContext context, final BrandSentToApprovalCommand command);

    BrandRoot moveToDraft(final SeDomainContext context, final BrandMoveToDraftCommand command);
}
