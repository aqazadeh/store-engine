package az.kon.academy.catalog.command.service.domain.core.service.attribute;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductAttributeRoot;
import az.kon.academy.catalog.command.service.domain.core.command.attribute.*;
import az.kon.academy.catalog.command.service.domain.core.exception.attribute.ProductAttributeDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.attribute.ProductAttributeDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductAttributeQueryPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryPort;

import java.util.List;

public class ProductAttributeManagementDomainServiceImpl implements ProductAttributeManagementDomainService {

    private final ProductCategoryQueryPort productCategoryQueryPort;
    private final ProductAttributeQueryPort productAttributeQueryPort;

    public ProductAttributeManagementDomainServiceImpl(ProductCategoryQueryPort productCategoryQueryPort,
                                                       ProductAttributeQueryPort productAttributeQueryPort) {
        this.productCategoryQueryPort = productCategoryQueryPort;
        this.productAttributeQueryPort = productAttributeQueryPort;
    }

    @Override
    public ProductAttributeRoot createAttribute(ProductAttributeCreateCommand command) {
        var category = productCategoryQueryPort.findByIdAndRowStatusActive(command.getCategoryId());
        if (category.isEmpty()) {
            throw new ProductAttributeDomainException(ProductAttributeDomainErrorCodes.CATEGORY_NOT_FOUND,
                    List.of(command.getCategoryId().toString())
            );
        }
        return ProductAttributeRoot.initialize(command);
    }

    @Override
    public ProductAttributeRoot changeInformation(ProductAttributeChangeInformationCommand command) {
        var productAttribute = productAttributeQueryPort.fetchByIdAndRowStatusActive(command.getAttributeId());
        return productAttribute.changeInformation(command);
    }

    @Override
    public ProductAttributeRoot changeCategory(ProductAttributeChangeCategoryCommand command) {
        var category = productCategoryQueryPort.findByIdAndRowStatusActive(command.getCategoryId());
        if (category.isEmpty()) {
            throw new ProductAttributeDomainException(ProductAttributeDomainErrorCodes.CATEGORY_NOT_FOUND,
                    List.of(command.getCategoryId().toString())
            );
        }
        var productAttribute = productAttributeQueryPort.fetchByIdAndRowStatusActive(command.getAttributeId());
        return productAttribute.changeCategory(command);
    }

    @Override
    public ProductAttributeRoot markAsRequired(ProductAttributeMarkAsRequiredCommand command) {
        var productAttribute = productAttributeQueryPort.fetchByIdAndRowStatusActive(command.getAttributeId());
        return productAttribute.markAsRequired();
    }

    @Override
    public ProductAttributeRoot markAsNonRequired(ProductAttributeMarkAsNonRequiredCommand command) {
        var productAttribute = productAttributeQueryPort.fetchByIdAndRowStatusActive(command.getAttributeId());
        return productAttribute.markAsNonRequired();
    }
}
