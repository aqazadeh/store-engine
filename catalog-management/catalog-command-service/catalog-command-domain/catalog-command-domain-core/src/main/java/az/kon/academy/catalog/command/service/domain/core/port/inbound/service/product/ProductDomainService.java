package az.kon.academy.catalog.command.service.domain.core.port.inbound.service.product;

import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductPriceRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.ProductStockRoot;
import az.kon.academy.catalog.command.service.domain.core.aggregate.management.rejection.ProductRejectionReasonRoot;
import az.kon.academy.catalog.command.service.domain.core.command.product.*;
import az.kon.academy.domain.core.SeDomainContext;

public interface ProductDomainService {
    ProductRoot createProduct(SeDomainContext context, ProductCreateCommand command);
    ProductRoot changeInformation(SeDomainContext context, ProductChangeInformationCommand command);
    ProductRoot archive(SeDomainContext context, ProductArchiveCommand command);
    ProductRoot assignBrand(SeDomainContext context, ProductAssignBrandCommand command);
    ProductRoot assignCategory(SeDomainContext context, ProductAssignCategoryCommand command);
    ProductRoot assignSpecification(SeDomainContext context, ProductAssignSpecificationCommand command);
    ProductRoot removeSpecification(SeDomainContext context, ProductRemoveSpecificationCommand command);
    ProductRoot addVariant(SeDomainContext context, ProductAddVariantCommand command);
    ProductRoot removeVariant(SeDomainContext context, ProductRemoveVariantCommand command);
    ProductRejectionReasonRoot createRejectionReason(SeDomainContext context, ProductCreateRejectionReasonCommand command);
    ProductPriceRoot createPrice(SeDomainContext context, ProductPriceCreateCommand command);
    ProductPriceRoot updatePrice(SeDomainContext context, ProductPriceUpdateCommand command);
    ProductStockRoot createStock(SeDomainContext context, ProductStockCreateCommand command);
    ProductStockRoot increaseStock(SeDomainContext context, ProductStockIncreaseCommand command);
    ProductStockRoot decreaseStock(SeDomainContext context, ProductStockDecreaseCommand command);
}
