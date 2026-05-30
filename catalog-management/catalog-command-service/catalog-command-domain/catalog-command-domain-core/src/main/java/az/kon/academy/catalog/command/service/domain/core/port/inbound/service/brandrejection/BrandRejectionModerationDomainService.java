package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brandrejection;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.BrandRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonChangeReasonCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brandrejection.BrandRejectionReasonRemoveCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface BrandRejectionModerationDomainService extends BrandRejectionDomainService permits BrandRejectionModerationDomainServiceImpl  {

    BrandRejectionReasonRoot add(SeDomainContext context, BrandRejectionReasonAddCommand command);

    BrandRejectionReasonRoot remove(SeDomainContext context, BrandRejectionReasonRemoveCommand command);

    BrandRejectionReasonRoot changeReason(SeDomainContext context, BrandRejectionReasonChangeReasonCommand command);
}
