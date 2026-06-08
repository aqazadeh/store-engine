package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface BrandDomainService permits BrandManagementDomainService, BrandModerationDomainService {
    BrandRoot create(final SeDomainContext context, final BrandCreateCommand command);

    BrandRoot changeInformation(final SeDomainContext context, final BrandChangeInformationCommand command);

    BrandRoot changeImage(final SeDomainContext context, final BrandChangeImageCommand command);
}
