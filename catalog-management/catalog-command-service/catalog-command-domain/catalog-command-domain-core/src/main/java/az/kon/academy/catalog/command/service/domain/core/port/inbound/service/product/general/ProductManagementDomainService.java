package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product.general;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantAddCommand;
import az.kon.academy.catalog.command.service.domain.core.command.productvariant.ProductVariantRemoveCommand;
import az.kon.academy.domain.core.SeDomainContext;

public sealed interface ProductManagementDomainService extends ProductDomainService permits ProductManagementDomainServiceImpl {
    ProductRoot createProduct(SeDomainContext context, ProductCreateCommand command);
    ProductRoot changeInformation(SeDomainContext context, ProductChangeInformationCommand command);
    ProductRoot archive(SeDomainContext context, ProductArchiveCommand command);
    ProductRoot assignBrand(SeDomainContext context, ProductAssignBrandCommand command);
    ProductRoot assignCategory(SeDomainContext context, ProductAssignCategoryCommand command);
    ProductRoot assignSpecification(SeDomainContext context, ProductAssignSpecificationCommand command);
    ProductRoot removeSpecification(SeDomainContext context, ProductRemoveSpecificationCommand command);
    ProductRoot addVariant(SeDomainContext context, ProductVariantAddCommand command);
    ProductRoot removeVariant(SeDomainContext context, ProductVariantRemoveCommand command);
    ProductRoot sentToApproval(SeDomainContext context, ProductSentToApprovalCommand command);
    ProductRoot moveToDraft(SeDomainContext context, ProductMoveToDraftCommand command);

}
