package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductCategoryModerationDomainService extends ProductCategoryDomainService permits ProductCategoryModerationDomainServiceImpl {

    ProductCategoryRoot createCategory(SeDomainContext context, ProductCategoryCreateCommand command);

    ProductCategoryRoot changeInformation(SeDomainContext context, ProductCategoryChangeInformationCommand command);

    ProductCategoryRoot changeImage(SeDomainContext context, ProductCategoryChangeImageCommand command);

    ProductCategoryRoot changeParent(SeDomainContext context, ProductCategoryChangeParentCommand command);

    ProductCategoryRoot archive(SeDomainContext context, ProductCategoryArchiveCommand command);

    ProductCategoryRoot activate(SeDomainContext context, ProductCategoryActivateCommand command);

    ProductCategoryRoot delete(SeDomainContext context, ProductCategoryDeleteCommand command);

}
