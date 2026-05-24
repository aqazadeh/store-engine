package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationRemoveCategoryAssignmentRequest;

public interface SpecificationRestPort {

    void create(SpecificationCreateRequest request);

    void changeInformation(SpecificationChangeInformationRequest request);

    void delete(SpecificationDeleteRequest request);

    void assignCategory(SpecificationAssignCategoryRequest request);

    void removeCategoryAssignment(SpecificationRemoveCategoryAssignmentRequest request);
}
