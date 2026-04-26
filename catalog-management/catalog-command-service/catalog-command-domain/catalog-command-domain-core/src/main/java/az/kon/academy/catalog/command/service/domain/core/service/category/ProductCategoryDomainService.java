package az.kon.academy.catalog.command.service.domain.core.service.category;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;

public interface ProductCategoryDomainService {

    ProductCategoryRoot createCategory(DomainContext context, ProductCategoryCreateCommand command);

    ProductCategoryRoot changeInformation(DomainContext context, ProductCategoryChangeInformationCommand command);

    ProductCategoryRoot changeImage(DomainContext context, ProductCategoryChangeImageCommand command);

    ProductCategoryRoot changeParent(DomainContext context, ProductCategoryChangeParentCommand command);

    ProductCategoryRoot archive(DomainContext context, ProductCategoryArchiveCommand command);

    ProductCategoryRoot activate(DomainContext context, ProductCategoryActivateCommand command);

}
