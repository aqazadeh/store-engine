package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.BrandQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductCategoryQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.port.outbound.ProductSpecificationQueryOutboundPort;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.product.ProductSpecificationAssignment;
import az.kon.academy.domain.core.SeDomainContext;

import java.util.List;

public final class ProductManagementDomainServiceImpl implements ProductManagementDomainService {

    @Override
    public ProductRoot createProduct(SeDomainContext context, ProductCreateCommand command) {
        var productCategoryQuery = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        productCategoryQuery.checkExitsById(command.getCategoryId());

        var brandQuery = context.getQueryPort(BrandQueryOutboundPort.class);
        brandQuery.checkExitsById(command.getBrandId());

        return ProductRoot.initialize(command);
    }

    @Override
    public ProductRoot changeInformation(SeDomainContext context, ProductChangeInformationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.changeInformation(command);
    }

    @Override
    public ProductRoot assignBrand(SeDomainContext context, ProductAssignBrandCommand command) {
        var brandQueryPort = context.getQueryPort(BrandQueryOutboundPort.class);
        brandQueryPort.fetchById(command.getBrandId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.assignBrand(command);
    }

    @Override
    public ProductRoot assignCategory(SeDomainContext context, ProductAssignCategoryCommand command) {
        var categoryQueryPort = context.getQueryPort(ProductCategoryQueryOutboundPort.class);
        categoryQueryPort.fetchById(command.getCategoryId());
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.assignCategory(command);
    }

    @Override
    public ProductRoot assignSpecifications(SeDomainContext context, ProductAssignSpecificationsCommand command) {
        var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        var specIds = command.getEntries().stream()
                .map(ProductAssignSpecificationsCommand.SpecificationEntry::specificationId)
                .toList();
        specificationQueryPort.checkAllExistByIds(specIds);

        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.assignSpecifications(command);
    }

    @Override
    public ProductRoot removeSpecification(SeDomainContext context, ProductRemoveSpecificationCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.removeSpecification(command);
    }

    @Override
    public ProductRoot sentToApproval(SeDomainContext context, ProductSentToApprovalCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());

        var specificationQueryPort = context.getQueryPort(ProductSpecificationQueryOutboundPort.class);
        var requiredSpecIds = specificationQueryPort.findRequiredByCategoryId(product.getCategoryId());
        var currentSpecIds = product.getSpecifications().stream()
                .map(ProductSpecificationAssignment::getSpecificationId)
                .toList();

        for (ProductSpecificationId required : requiredSpecIds) {
            if (!currentSpecIds.contains(required)) {
                throw new ProductDomainException(
                        ProductDomainErrorCodes.REQUIRED_SPECIFICATION_MISSING,
                        List.of(required.value().toString())
                );
            }
        }

        return product.sentToApproval();
    }

    @Override
    public ProductRoot moveToDraft(SeDomainContext context, ProductMoveToDraftCommand command) {
        var productQueryPort = context.getQueryPort(ProductQueryOutboundPort.class);
        var product = productQueryPort.fetchByIdAndMerchantId(command.getProductId(), command.getMerchantId());
        return product.moveToDraft();
    }
}
