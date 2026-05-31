package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationCategoryAssignment;
import az.kon.academy.domain.core.SeDomainContext;

public final class ProductSpecificationModerationDomainServiceImpl implements ProductSpecificationModerationDomainService {

    @Override
    public ProductSpecificationRoot create(final SeDomainContext context, final ProductSpecificationCreateCommand command) {
        return ProductSpecificationRoot.initialize(command);
    }

    @Override
    public ProductSpecificationRoot changeInformation(final SeDomainContext context, final ProductSpecificationChangeInformationCommand command) {
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.changeInformation(command);
    }

    @Override
    public ProductSpecificationRoot delete(final SeDomainContext context, final ProductSpecificationDeleteCommand command) {
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getSpecificationId());
        return specification.delete();
    }

    @Override //FIXME
    public ProductSpecificationRoot assignCategory(final SeDomainContext context, final ProductSpecificationAssignCategoryCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getCategoryId());
        final var assignment = SpecificationCategoryAssignment.initialize(category.getRootID(), command.isRequired());
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.assignCategory(assignment);
    }

    @Override //FIXME when category is Deleted remove also assignment
    public ProductSpecificationRoot removeCategoryAssignment(final SeDomainContext context, final ProductSpecificationRemoveCategoryAssignmentCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getCategoryId());
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchByIdAndRowStatusActive(command.getProductSpecificationId());
        return specification.removeCategoryAssignment(command);
    }
}
