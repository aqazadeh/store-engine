package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.management.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;

import java.util.Objects;

public class ProductCategoryDomainServiceImpl implements ProductCategoryDomainService {

    @Override
    public ProductCategoryRoot createCategory(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryCreateCommand command) {
        return ProductCategoryRoot.initialize(command);
    }

    @Override
    public ProductCategoryRoot changeInformation(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeInformationCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.changeInformation(command);
    }

    @Override
    public ProductCategoryRoot changeImage(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeImageCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.changeImage(command);
    }

    @Override
    public ProductCategoryRoot changeParent(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryChangeParentCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        if (Objects.isNull(command.getParentId()))
            return category.removeParent();

        var parentCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var parentCategory = parentCategoryQueryPort.fetchByIdAndRowStatusActive(command.getParentId());
        return category.changeParent(parentCategory.getRootID());
    }

    @Override
    public ProductCategoryRoot archive(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryArchiveCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.markAsArchived();
    }

    @Override
    public ProductCategoryRoot activate(az.kon.academy.domain.core.SeDomainContext context, ProductCategoryActivateCommand command) {
        var productCategoryQueryPort = context.getQueryPort(ProductCategoryQueryPort.class);
        var category = productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.markAsActive();
    }
}
