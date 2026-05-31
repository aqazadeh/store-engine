package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;
import java.util.Objects;

public final class ProductCategoryModerationDomainServiceImpl implements ProductCategoryModerationDomainService {

    @Override
    public ProductCategoryRoot createCategory(final SeDomainContext context, final ProductCategoryCreateCommand command) {
        return ProductCategoryRoot.initialize(command);
    }

    @Override
    public ProductCategoryRoot changeInformation(final SeDomainContext context, final ProductCategoryChangeInformationCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.changeInformation(command);
    }

    @Override
    public ProductCategoryRoot changeImage(final SeDomainContext context, final ProductCategoryChangeImageCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.changeImage(command);
    }

    @Override
    public ProductCategoryRoot changeParent(final SeDomainContext context, final ProductCategoryChangeParentCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        if (Objects.isNull(command.getParentId()))
            return category.removeParent();

        final var parentCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var parentCategory = parentCategoryQueryPort.fetchById(command.getParentId());
        return category.changeParent(parentCategory.getRootID());
    }

    @Override
    public ProductCategoryRoot archive(final SeDomainContext context, final ProductCategoryArchiveCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.archive();
    }

    @Override
    public ProductCategoryRoot activate(final SeDomainContext context, final ProductCategoryActivateCommand command) {
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.activate();
    }

    @Override
    public ProductCategoryRoot delete(final SeDomainContext context, final ProductCategoryDeleteCommand command) {
        final var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        final var existsProduct = productQueryPort.exitsByCategoryId(command.getProductCategoryId());
        if (existsProduct) {
            throw new ProductCategoryDomainException(
                    ProductCategoryDomainErrorCodes.HAS_ACTIVE_PRODUCT,
                    List.of(command.getProductCategoryId().toString())
            );
        }
        final var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        final var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.delete();
    }
}
