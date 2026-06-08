package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;
import java.util.Objects;

public final class ProductCategoryModerationDomainServiceImpl implements ProductCategoryModerationDomainService {

    @Override
    public ProductCategoryRoot createCategory(final SeDomainContext context, final ProductCategoryCreateCommand command) {
        final var categoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);

        if (command.getParentId() != null) {
            categoryQuery.fetchById(command.getParentId());
        }

        categoryQuery.checkNameUniqueByParent(command.getName(), command.getParentId());

        return ProductCategoryRoot.initialize(command);
    }

    @Override
    public ProductCategoryRoot changeInformation(final SeDomainContext context, final ProductCategoryChangeInformationCommand command) {
        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        return category.changeInformation(command);
    }

    @Override
    public ProductCategoryRoot changeImage(final SeDomainContext context, final ProductCategoryChangeImageCommand command) {
        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        return category.changeImage(command);
    }

    @Override
    public ProductCategoryRoot changeParent(final SeDomainContext context, final ProductCategoryChangeParentCommand command) {
        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        if (Objects.isNull(command.getParentId()))
            return category.removeParent();

        if (command.getProductCategoryId().equals(command.getParentId())) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.CIRCULAR_PARENT_REFERENCE,
                    List.of(command.getProductCategoryId().toString())
            );
        }

        productCategoryQuery.checkIsNotDescendant(command.getProductCategoryId(), command.getParentId());

        return category.changeParent(command.getParentId());
    }

    @Override
    public ProductCategoryRoot archive(final SeDomainContext context, final ProductCategoryArchiveCommand command) {
        final var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        final var existsProduct = productQuery.exitsByCategoryId(command.getProductCategoryId());
        if (existsProduct) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.HAS_ACTIVE_PRODUCT,
                    List.of(command.getProductCategoryId().toString())
            );
        }

        final var productSpecificationQuery = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var existsSpecificationAssignment = productSpecificationQuery.existsAssignmentByCategoryId(command.getProductCategoryId());
        if (existsSpecificationAssignment) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.HAS_ACTIVE_SPECIFICATION_ASSIGNMENT,
                    List.of(command.getProductCategoryId().toString())
            );
        }

        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        return category.archive();
    }

    @Override
    public ProductCategoryRoot activate(final SeDomainContext context, final ProductCategoryActivateCommand command) {
        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        return category.activate();
    }

    @Override
    public ProductCategoryRoot delete(final SeDomainContext context, final ProductCategoryDeleteCommand command) {
        final var productQuery = context.getQueryPort(ProductQueryOutboundPort.class);
        final var existsProduct = productQuery.exitsByCategoryId(command.getProductCategoryId());
        if (existsProduct) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.HAS_ACTIVE_PRODUCT,
                    List.of(command.getProductCategoryId().toString())
            );
        }

        final var productSpecificationQuery = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        final var existsSpecificationAssignment = productSpecificationQuery.existsAssignmentByCategoryId(command.getProductCategoryId());
        if(existsSpecificationAssignment) {
            throw  new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.HAS_ACTIVE_SPECIFICATION_ASSIGNMENT,
                    List.of(command.getProductCategoryId().toString())
            );
        }
        final var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQuery.fetchById(command.getProductCategoryId());
        return category.delete();
    }
}
