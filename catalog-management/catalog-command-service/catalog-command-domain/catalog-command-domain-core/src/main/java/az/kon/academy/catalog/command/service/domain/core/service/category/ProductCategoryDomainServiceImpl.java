package az.kon.academy.catalog.command.service.domain.core.service.category;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductCategoryRoot;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryEntityNotFoundException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;

import java.util.List;
import java.util.Objects;

public class ProductCategoryDomainServiceImpl implements ProductCategoryDomainService {

    private final ProductCategoryQueryPort productCategoryQueryPort;

    public ProductCategoryDomainServiceImpl(ProductCategoryQueryPort productCategoryQueryPort) {
        this.productCategoryQueryPort = productCategoryQueryPort;
    }

    @Override
    public ProductCategoryRoot createCategory(ProductCategoryCreateCommand command) {
        return ProductCategoryRoot.initialize(command);
    }

    @Override
    public ProductCategoryRoot changeInformation(ProductCategoryChangeInformationCommand command) {
        var category = this.productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.changeInformation(command);
    }

    @Override
    public ProductCategoryRoot changeImage(ProductCategoryChangeImageCommand command) {
        var category = this.productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.changeImage(command);
    }

    @Override
    public ProductCategoryRoot changeParent(ProductCategoryChangeParentCommand command) {
        var category = this.productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        if (Objects.isNull(command.getParentId()))
            return category.removeParent();

        var parentCategory = this.productCategoryQueryPort.findByIdAndRowStatusActive(command.getParentId());
        if (parentCategory.isEmpty()) {
            throw new ProductCategoryEntityNotFoundException(
                    ProductCategoryDomainErrorCodes.PARENT_NOT_FOUND,
                    List.of(command.getParentId().toString())
            );
        }
        return category.changeParent(command);
    }

    @Override
    public ProductCategoryRoot archive(ProductCategoryArchiveCommand command) {
        var category = this.productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.markAsArchived();
    }

    @Override
    public ProductCategoryRoot activate(ProductCategoryActivateCommand command) {
        var category = this.productCategoryQueryPort.fetchByIdAndRowStatusActive(command.getProductCategoryId());
        return category.markAsActive();
    }
}
