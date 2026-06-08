package az.kon.academy.catalog.command.service.application.service.port.inbound.specification;

import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationRemoveCategoryAssignmentRequest;

public interface ProductSpecificationModerationInboundPort {

    void create(ProductSpecificationCreateRequest request);

    void changeInformation(ProductSpecificationChangeInformationRequest request);

    void delete(ProductSpecificationDeleteRequest request);

    void assignCategory(ProductSpecificationAssignCategoryRequest request);

    void removeCategoryAssignment(ProductSpecificationRemoveCategoryAssignmentRequest request);
}
