package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductCategoryModerationDomainService extends ProductCategoryDomainService permits ProductCategoryModerationDomainServiceImpl {

    ProductCategoryRoot createCategory(final SeDomainContext context, final ProductCategoryCreateCommand command);

    ProductCategoryRoot changeInformation(final SeDomainContext context, final ProductCategoryChangeInformationCommand command);

    ProductCategoryRoot changeImage(final SeDomainContext context, final ProductCategoryChangeImageCommand command);

    ProductCategoryRoot changeParent(final SeDomainContext context, final ProductCategoryChangeParentCommand command);

    ProductCategoryRoot archive(final SeDomainContext context, final ProductCategoryArchiveCommand command);

    ProductCategoryRoot activate(final SeDomainContext context, final ProductCategoryActivateCommand command);

    ProductCategoryRoot delete(final SeDomainContext context, final ProductCategoryDeleteCommand command);

}
