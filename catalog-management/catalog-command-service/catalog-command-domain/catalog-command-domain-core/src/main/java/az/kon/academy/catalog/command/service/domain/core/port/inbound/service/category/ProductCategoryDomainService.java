package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;

public interface ProductCategoryDomainService {

    ProductCategoryRoot createCategory(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryCreateCommand command);

    ProductCategoryRoot changeInformation(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeInformationCommand command);

    ProductCategoryRoot changeImage(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeImageCommand command);

    ProductCategoryRoot changeParent(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeParentCommand command);

    ProductCategoryRoot archive(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryArchiveCommand command);

    ProductCategoryRoot activate(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryActivateCommand command);

}
