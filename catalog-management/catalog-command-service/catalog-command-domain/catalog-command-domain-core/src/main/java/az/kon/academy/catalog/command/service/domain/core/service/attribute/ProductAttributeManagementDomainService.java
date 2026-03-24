package az.kon.academy.catalog.command.service.domain.core.service.attribute;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductAttributeRoot;
import az.kon.academy.catalog.command.service.domain.core.command.attribute.*;

public interface ProductAttributeManagementDomainService {
    ProductAttributeRoot createAttribute(ProductAttributeCreateCommand command);

    ProductAttributeRoot changeInformation(ProductAttributeChangeInformationCommand command);

    ProductAttributeRoot changeCategory(ProductAttributeChangeCategoryCommand command);

    ProductAttributeRoot markAsRequired(ProductAttributeMarkAsRequiredCommand command);

    ProductAttributeRoot markAsNonRequired(ProductAttributeMarkAsNonRequiredCommand command);
}
