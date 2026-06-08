package az.kon.academy.catalog.command.service.application.service.port.inbound.specification;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationAssignCategoryRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationCreateRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.ProductSpecificationRemoveCategoryAssignmentRequest;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.ProductSpecificationAssignCategoryCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.ProductSpecificationChangeInformationCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.ProductSpecificationCreateCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.ProductSpecificationDeleteCommandHandler;
import az.kon.academy.catalog.command.service.application.service.handler.command.specification.ProductSpecificationRemoveCategoryAssignmentCommandHandler;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationAssignCategoryCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationChangeInformationCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationCreateCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationDeleteCommand;
import az.kon.academy.catalog.command.service.domain.core.command.specification.ProductSpecificationRemoveCategoryAssignmentCommand;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.ProductSpecificationId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.specification.SpecificationName;

@InputAdapter
class ProductSpecificationModerationInboundAdapter implements ProductSpecificationModerationInboundPort {

    private final ProductSpecificationCreateCommandHandler productSpecificationCreateCommandHandler;
    private final ProductSpecificationChangeInformationCommandHandler productSpecificationChangeInformationCommandHandler;
    private final ProductSpecificationDeleteCommandHandler productSpecificationDeleteCommandHandler;
    private final ProductSpecificationAssignCategoryCommandHandler productSpecificationAssignCategoryCommandHandler;
    private final ProductSpecificationRemoveCategoryAssignmentCommandHandler productSpecificationRemoveCategoryAssignmentCommandHandler;

    public ProductSpecificationModerationInboundAdapter(ProductSpecificationCreateCommandHandler productSpecificationCreateCommandHandler,
                                                        ProductSpecificationChangeInformationCommandHandler productSpecificationChangeInformationCommandHandler,
                                                        ProductSpecificationDeleteCommandHandler productSpecificationDeleteCommandHandler,
                                                        ProductSpecificationAssignCategoryCommandHandler productSpecificationAssignCategoryCommandHandler,
                                                        ProductSpecificationRemoveCategoryAssignmentCommandHandler productSpecificationRemoveCategoryAssignmentCommandHandler) {
        this.productSpecificationCreateCommandHandler = productSpecificationCreateCommandHandler;
        this.productSpecificationChangeInformationCommandHandler = productSpecificationChangeInformationCommandHandler;
        this.productSpecificationDeleteCommandHandler = productSpecificationDeleteCommandHandler;
        this.productSpecificationAssignCategoryCommandHandler = productSpecificationAssignCategoryCommandHandler;
        this.productSpecificationRemoveCategoryAssignmentCommandHandler = productSpecificationRemoveCategoryAssignmentCommandHandler;
    }

    @Override
    public void create(ProductSpecificationCreateRequest request) {
        var command = ProductSpecificationCreateCommand.builder()
                .name(SpecificationName.of(request.getName()))
                .description(SpecificationDescription.of(request.getDescription()))
                .build();
        this.productSpecificationCreateCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(ProductSpecificationChangeInformationRequest request) {
        var command = ProductSpecificationChangeInformationCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .name(SpecificationName.of(request.getName()))
                .description(SpecificationDescription.of(request.getDescription()))
                .build();
        this.productSpecificationChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void delete(ProductSpecificationDeleteRequest request) {
        var command = ProductSpecificationDeleteCommand.builder()
                .specificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .build();
        this.productSpecificationDeleteCommandHandler.handle(command);
    }

    @Override
    public void assignCategory(ProductSpecificationAssignCategoryRequest request) {
        var command = ProductSpecificationAssignCategoryCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .isRequired(request.isRequired())
                .build();
        this.productSpecificationAssignCategoryCommandHandler.handle(command);
    }

    @Override
    public void removeCategoryAssignment(ProductSpecificationRemoveCategoryAssignmentRequest request) {
        var command = ProductSpecificationRemoveCategoryAssignmentCommand.builder()
                .productSpecificationId(ProductSpecificationId.from(request.getSpecificationId()))
                .categoryId(ProductCategoryId.from(request.getCategoryId()))
                .build();
        this.productSpecificationRemoveCategoryAssignmentCommandHandler.handle(command);
    }
}
