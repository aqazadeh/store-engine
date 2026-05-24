package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryActivateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryArchiveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryChangeParentRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.ProductCategoryCreateRequest;

public interface CategoryRestPort {

    void create(ProductCategoryCreateRequest request);

    void activate(ProductCategoryActivateRequest request);

    void archive(ProductCategoryArchiveRequest request);

    void changeImage(ProductCategoryChangeImageRequest request);

    void changeInformation(ProductCategoryChangeInformationRequest request);

    void changeParent(ProductCategoryChangeParentRequest request);
}
