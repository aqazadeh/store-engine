package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeImageCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.brand.BrandSentToApprovalCommand;

public interface BrandModificationDomainService {
    BrandRoot createBrand(BrandCreateCommand command);

    BrandRoot changeInformation(BrandChangeInformationCommand command);

    BrandRoot changeImage(BrandChangeImageCommand command);

    BrandRoot sentToApproval(BrandSentToApprovalCommand command);

    BrandRoot moveToDraft(BrandSentToApprovalCommand command);


}
