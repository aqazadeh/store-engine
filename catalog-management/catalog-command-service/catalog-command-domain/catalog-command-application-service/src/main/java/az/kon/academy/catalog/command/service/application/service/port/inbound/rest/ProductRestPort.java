package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAddVariantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductArchiveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignBrandRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductAssignSpecificationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductCreateRejectionReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductPriceCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductPriceUpdateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductRemoveSpecificationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductRemoveVariantRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockDecreaseRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.product.ProductStockIncreaseRequest;

public interface ProductRestPort {

    void create(ProductCreateRequest request);

    void changeInformation(ProductChangeInformationRequest request);

    void archive(ProductArchiveRequest request);

    void assignBrand(ProductAssignBrandRequest request);

    void assignCategory(ProductAssignCategoryRequest request);

    void assignSpecification(ProductAssignSpecificationRequest request);

    void removeSpecification(ProductRemoveSpecificationRequest request);

    void addVariant(ProductAddVariantRequest request);

    void removeVariant(ProductRemoveVariantRequest request);

    void createRejectionReason(ProductCreateRejectionReasonRequest request);

    void createPrice(ProductPriceCreateRequest request);

    void updatePrice(ProductPriceUpdateRequest request);

    void createStock(ProductStockCreateRequest request);

    void increaseStock(ProductStockIncreaseRequest request);

    void decreaseStock(ProductStockDecreaseRequest request);
}
