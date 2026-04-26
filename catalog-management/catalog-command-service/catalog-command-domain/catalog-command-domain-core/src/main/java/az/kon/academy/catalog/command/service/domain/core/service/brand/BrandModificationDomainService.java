package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandSentToApprovalCommand;

public interface BrandModificationDomainService {
    BrandRoot createBrand(DomainContext context, BrandCreateCommand command);

    BrandRoot changeInformation(DomainContext context, BrandChangeInformationCommand command);

    BrandRoot changeImage(DomainContext context, BrandChangeImageCommand command);

    BrandRoot sentToApproval(DomainContext context, BrandSentToApprovalCommand command);

    BrandRoot moveToDraft(DomainContext context, BrandSentToApprovalCommand command);


}
