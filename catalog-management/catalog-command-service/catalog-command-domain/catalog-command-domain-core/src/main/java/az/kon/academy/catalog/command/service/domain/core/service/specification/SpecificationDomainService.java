package az.kon.academy.catalog.command.service.domain.core.service.specification;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;

public interface SpecificationDomainService {
    ProductSpecificationRoot createAttribute(DomainContext context, SpecificationCreateCommand command);

    ProductSpecificationRoot changeInformation(DomainContext context, SpecificationChangeInformationCommand command);

    ProductSpecificationRoot assignCategory(DomainContext context, SpecificationAssignCategoryCommand command);

    ProductSpecificationRoot removeCategoryAssignment(DomainContext context, SpecificationRemoveCategoryAssignmentCommand command);

    ProductSpecificationRoot delete(DomainContext context, SpecificationDeleteCommand command);
}
