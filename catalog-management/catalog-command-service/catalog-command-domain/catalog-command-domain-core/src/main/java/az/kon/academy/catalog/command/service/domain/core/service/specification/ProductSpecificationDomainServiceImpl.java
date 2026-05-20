package az.kon.academy.catalog.command.service.domain.core.service.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.SpecificationQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;
import az.kon.academy.domain.core.SeDomainContext;

public class ProductSpecificationDomainServiceImpl implements ProductSpecificationDomainService {

    @Override
    public ProductSpecificationRoot create(SeDomainContext context, SpecificationCreateCommand command) {
        return ProductSpecificationRoot.initialize(command);
    }

    @Override
    public ProductSpecificationRoot changeInformation(SeDomainContext context, SpecificationChangeInformationCommand command) {
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.changeInformation(command);
    }

    @Override
    public ProductSpecificationRoot assignCategory(SeDomainContext context, SpecificationAssignCategoryCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getCategoryId());
        var assignment = SpecificationCategoryAssignment.initialize(category.getRootID(), command.isRequired());
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.assignCategory(assignment);
    }

    @Override
    public ProductSpecificationRoot removeCategoryAssignment(SeDomainContext context, SpecificationRemoveCategoryAssignmentCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getCategoryId());
        var assignment = SpecificationCategoryAssignment.initialize(category.getRootID(), command.isRequired());
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.removeCategoryAssignment(assignment);
    }

    @Override
    public ProductSpecificationRoot delete(SeDomainContext context, SpecificationDeleteCommand command) {
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getSpecificationId());
        return specification.markAsDeleted();
    }
}
