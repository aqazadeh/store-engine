package az.kon.academy.catalog.command.service.application.service.port.inbound.rest;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.SpecificationRemoveCategoryAssignmentRequest;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.SpecificationAssignCategoryCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.SpecificationChangeInformationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.SpecificationCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.SpecificationDeleteCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.SpecificationRemoveCategoryAssignmentCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationDeleteCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.SpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;

@InputAdapter
class SpecificationInboundAdapter implements SpecificationInboundPort {

    private final SpecificationCreateCommandHandler specificationCreateCommandHandler;
    private final SpecificationChangeInformationCommandHandler specificationChangeInformationCommandHandler;
    private final SpecificationDeleteCommandHandler specificationDeleteCommandHandler;
    private final SpecificationAssignCategoryCommandHandler specificationAssignCategoryCommandHandler;
    private final SpecificationRemoveCategoryAssignmentCommandHandler specificationRemoveCategoryAssignmentCommandHandler;

    public SpecificationInboundAdapter(SpecificationCreateCommandHandler specificationCreateCommandHandler,
                                       SpecificationChangeInformationCommandHandler specificationChangeInformationCommandHandler,
                                       SpecificationDeleteCommandHandler specificationDeleteCommandHandler,
                                       SpecificationAssignCategoryCommandHandler specificationAssignCategoryCommandHandler,
                                       SpecificationRemoveCategoryAssignmentCommandHandler specificationRemoveCategoryAssignmentCommandHandler) {
        this.specificationCreateCommandHandler = specificationCreateCommandHandler;
        this.specificationChangeInformationCommandHandler = specificationChangeInformationCommandHandler;
        this.specificationDeleteCommandHandler = specificationDeleteCommandHandler;
        this.specificationAssignCategoryCommandHandler = specificationAssignCategoryCommandHandler;
        this.specificationRemoveCategoryAssignmentCommandHandler = specificationRemoveCategoryAssignmentCommandHandler;
    }

    @Override
    public void create(SpecificationCreateRequest request) {
        var command = SpecificationCreateCommand.builder()
                .name(SpecificationName.of(request.getName()))
                .description(SpecificationDescription.of(request.getDescription()))
                .build();
        this.specificationCreateCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(SpecificationChangeInformationRequest request) {
        var command = SpecificationChangeInformationCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .name(SpecificationName.of(request.getName()))
                .description(SpecificationDescription.of(request.getDescription()))
                .build();
        this.specificationChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void delete(SpecificationDeleteRequest request) {
        var command = SpecificationDeleteCommand.builder()
                .specificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .build();
        this.specificationDeleteCommandHandler.handle(command);
    }

    @Override
    public void assignCategory(SpecificationAssignCategoryRequest request) {
        var command = SpecificationAssignCategoryCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .isRequired(request.isRequired())
                .build();
        this.specificationAssignCategoryCommandHandler.handle(command);
    }

    @Override
    public void removeCategoryAssignment(SpecificationRemoveCategoryAssignmentRequest request) {
        var command = SpecificationRemoveCategoryAssignmentCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .isRequired(request.isRequired())
                .build();
        this.specificationRemoveCategoryAssignmentCommandHandler.handle(command);
    }
}
