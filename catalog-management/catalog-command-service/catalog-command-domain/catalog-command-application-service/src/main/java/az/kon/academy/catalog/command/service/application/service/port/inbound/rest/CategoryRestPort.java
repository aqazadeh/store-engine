package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryActivateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryArchiveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeParentRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryCreateRequest;

public interface CategoryRestPort {

    void createCategory(ProductCategoryCreateRequest request);

    void activateCategory(ProductCategoryActivateRequest request);

    void archiveCategory(ProductCategoryArchiveRequest request);

    void changeImage(ProductCategoryChangeImageRequest request);

    void changeInformation(ProductCategoryChangeInformationRequest request);

    void changeParent(ProductCategoryChangeParentRequest request);
}
