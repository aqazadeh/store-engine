package az.kon.academy.catalog.command.service.domain.core.service.specification;

import az.kon.academy.catalog.command.service.domain.core.DomainContext;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.SpecificationQueryPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;

public class SpecificationDomainServiceImpl implements SpecificationDomainService {

    @Override
    public ProductSpecificationRoot createAttribute(DomainContext context, SpecificationCreateCommand command) {
        return ProductSpecificationRoot.initialize(command);
    }

    @Override
    public ProductSpecificationRoot changeInformation(DomainContext context, SpecificationChangeInformationCommand command) {
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.changeInformation(command);
    }

    @Override
    public ProductSpecificationRoot assignCategory(DomainContext context, SpecificationAssignCategoryCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getCategoryId());
        var assignment = SpecificationCategoryAssignment.initialize(category.getRootID(), command.isRequired());
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.assignCategory(assignment);
    }

    @Override
    public ProductSpecificationRoot removeCategoryAssignment(DomainContext context, SpecificationRemoveCategoryAssignmentCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getCategoryId());
        var assignment = SpecificationCategoryAssignment.initialize(category.getRootID(), command.isRequired());
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.removeCategoryAssignment(assignment);
    }

    @Override
    public ProductSpecificationRoot delete(DomainContext context, SpecificationDeleteCommand command) {
        var specificationQueryPort = context.getQueryPort(SpecificationQueryPort.class);
        var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getSpecificationId());
        return specification.markAsDeleted();
    }
}
