package az.kon.academy.catalog.command.service.domain.core.service.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.domain.core.SeDomainContext;

public interface ProductSpecificationDomainService {
    ProductSpecificationRoot create(SeDomainContext context, SpecificationCreateCommand command);

    ProductSpecificationRoot changeInformation(SeDomainContext context, SpecificationChangeInformationCommand command);

    ProductSpecificationRoot assignCategory(SeDomainContext context, SpecificationAssignCategoryCommand command);

    ProductSpecificationRoot removeCategoryAssignment(SeDomainContext context, SpecificationRemoveCategoryAssignmentCommand command);

    ProductSpecificationRoot delete(SeDomainContext context, SpecificationDeleteCommand command);
}
