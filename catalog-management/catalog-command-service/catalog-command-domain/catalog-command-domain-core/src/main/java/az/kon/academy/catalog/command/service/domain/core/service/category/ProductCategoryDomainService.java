package az.kon.academy.catalog.command.service.domain.core.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;

public interface ProductCategoryDomainService {

    ProductCategoryRoot createCategory(ProductCategoryCreateCommand command);

    ProductCategoryRoot changeInformation(ProductCategoryChangeInformationCommand command);

    ProductCategoryRoot changeImage(ProductCategoryChangeImageCommand command);

    ProductCategoryRoot changeParent(ProductCategoryChangeParentCommand command);

    ProductCategoryRoot archive(ProductCategoryArchiveCommand command);

    ProductCategoryRoot activate(ProductCategoryActivateCommand command);

}
