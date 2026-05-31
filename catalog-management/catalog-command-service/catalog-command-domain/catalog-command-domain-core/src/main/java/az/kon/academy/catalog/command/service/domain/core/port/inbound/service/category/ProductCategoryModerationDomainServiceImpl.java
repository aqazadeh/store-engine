package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.Objects;

public final class ProductCategoryModerationDomainServiceImpl implements ProductCategoryModerationDomainService {

    @Override
    public ProductCategoryRoot createCategory(SeDomainContext context, ProductCategoryCreateCommand command) {
        return ProductCategoryRoot.initialize(command);
    }

    @Override
    public ProductCategoryRoot changeInformation(SeDomainContext context, ProductCategoryChangeInformationCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.changeInformation(command);
    }

    @Override
    public ProductCategoryRoot changeImage(SeDomainContext context, ProductCategoryChangeImageCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.changeImage(command);
    }

    @Override
    public ProductCategoryRoot changeParent(SeDomainContext context, ProductCategoryChangeParentCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        if (Objects.isNull(command.getParentId()))
            return category.removeParent();

        var parentCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var parentCategory = parentCategoryQueryPort.fetchById(command.getParentId());
        return category.changeParent(parentCategory.getRootID());
    }

    @Override
    public ProductCategoryRoot archive(SeDomainContext context, ProductCategoryArchiveCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.archive();
    }

    @Override
    public ProductCategoryRoot activate(SeDomainContext context, ProductCategoryActivateCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.activate();
    }

    @Override
    public ProductCategoryRoot delete(SeDomainContext context, ProductCategoryDeleteCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        var category = productCategoryQueryPort.fetchById(command.getProductCategoryId());
        return category.delete();
    }
}
