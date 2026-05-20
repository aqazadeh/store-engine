package az.kon.academy.catalog.command.service.domain.core.service.brand;

import az.kon.academy.catalog.command.service.domain.core.aggregate.BrandRoot;
import az.kon.academy.catalog.command.service.domain.core.command.brand.merchant.*;
import az.kon.academy.domain.core.SeDomainContext;

public interface BrandModificationDomainService {
    BrandRoot createForMerchant(SeDomainContext context, BrandCreateForMerchantCommand command);

    BrandRoot changeInformation(SeDomainContext context, BrandChangeInformationCommand command);

    BrandRoot changeImage(SeDomainContext context, BrandChangeImageCommand command);

    BrandRoot sentToApproval(SeDomainContext context, BrandSentToApprovalCommand command);

    BrandRoot moveToDraft(SeDomainContext context, BrandMoveToDraftCommand command);


}
