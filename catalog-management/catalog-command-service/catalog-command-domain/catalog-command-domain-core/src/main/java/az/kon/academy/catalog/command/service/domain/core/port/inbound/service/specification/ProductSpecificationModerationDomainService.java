package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductSpecificationModerationDomainService extends ProductSpecificationDomainService permits ProductSpecificationModerationDomainServiceImpl {
    ProductSpecificationRoot create(final SeDomainContext context, final ProductSpecificationCreateCommand command);

    ProductSpecificationRoot changeInformation(final SeDomainContext context, final ProductSpecificationChangeInformationCommand command);

    ProductSpecificationRoot delete(final SeDomainContext context, final ProductSpecificationDeleteCommand command);

    ProductSpecificationRoot assignCategory(final SeDomainContext context, final ProductSpecificationAssignCategoryCommand command);

    ProductSpecificationRoot removeCategoryAssignment(final SeDomainContext context, final ProductSpecificationRemoveCategoryAssignmentCommand command);
}
