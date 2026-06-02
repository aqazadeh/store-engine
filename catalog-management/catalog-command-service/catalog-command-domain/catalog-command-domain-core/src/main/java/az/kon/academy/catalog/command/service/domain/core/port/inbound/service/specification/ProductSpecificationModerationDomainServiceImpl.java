package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.specification;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductSpecificationRoot;
import az.kon.academy.catalog.command.service.domain.core.command.specification.*;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.ProductSpecificationDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.specification.ProductSpecificationDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class ProductSpecificationModerationDomainServiceImpl implements ProductSpecificationModerationDomainService {

    @Override
    public ProductSpecificationRoot create(final SeDomainContext context, final ProductSpecificationCreateCommand command) {
        return ProductSpecificationRoot.initialize(command);
    }

    @Override
    public ProductSpecificationRoot changeInformation(final SeDomainContext context, final ProductSpecificationChangeInformationCommand command) {
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchById(command.getProductSpecificationId());
        return specification.changeInformation(command);
    }

    @Override
    public ProductSpecificationRoot delete(final SeDomainContext context, final ProductSpecificationDeleteCommand command) {
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchById(command.getSpecificationId());
        return specification.delete();
    }

    @Override
    public ProductSpecificationRoot assignCategory(final SeDomainContext context, final ProductSpecificationAssignCategoryCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        if(productCategoryQueryPort.exitsByCategoryId(command.getCategoryId())) {
            throw new ProductSpecificationDomainException(
                    ProductSpecificationDomainErrorCodes.CATEGORY_NOT_FOUND,
                    List.of(command.getCategoryId().toString())
            );
        }
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchById(command.getProductSpecificationId());
        return specification.assignCategory(command);
    }

    @Override
    public ProductSpecificationRoot removeCategoryAssignment(final SeDomainContext context, final ProductSpecificationRemoveCategoryAssignmentCommand command) {
        final var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var specification = specificationQueryPort.fetchById(command.getProductSpecificationId());
        return specification.removeCategoryAssignment(command);
    }
}
