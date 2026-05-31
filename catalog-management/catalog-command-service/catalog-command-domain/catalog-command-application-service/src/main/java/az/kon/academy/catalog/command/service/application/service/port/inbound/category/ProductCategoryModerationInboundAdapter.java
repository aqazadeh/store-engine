package az.kon.academy.catalog.command.service.application.service.port.inbound.category;

import az.kon.academy.application.core.annotation.InputAdapter;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;
import az.kon.academy.catalog.command.service.application.service.handler.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.command.category.*;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryDescription;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryId;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryName;
import az.kon.academy.catalog.command.service.domain.core.vo.management.category.ProductCategoryPath;

@InputAdapter
class ProductCategoryModerationInboundAdapter implements ProductCategoryModerationInboundPort {

    private final ProductCategoryCreateCommandHandler productCategoryCreateCommandHandler;
    private final ProductCategoryActivateCommandHandler productCategoryActivateCommandHandler;
    private final ProductCategoryArchiveCommandHandler productCategoryArchiveCommandHandler;
    private final ProductCategoryDeleteCommandHandler productCategoryDeleteCommandHandler;
    private final ProductCategoryChangeImageCommandHandler productCategoryChangeImageCommandHandler;
    private final ProductCategoryChangeInformationCommandHandler productCategoryChangeInformationCommandHandler;
    private final ProductCategoryChangeParentCommandHandler productCategoryChangeParentCommandHandler;

    public ProductCategoryModerationInboundAdapter(ProductCategoryCreateCommandHandler productCategoryCreateCommandHandler,
                                                   ProductCategoryActivateCommandHandler productCategoryActivateCommandHandler,
                                                   ProductCategoryArchiveCommandHandler productCategoryArchiveCommandHandler,
                                                   ProductCategoryDeleteCommandHandler productCategoryDeleteCommandHandler,
                                                   ProductCategoryChangeImageCommandHandler productCategoryChangeImageCommandHandler,
                                                   ProductCategoryChangeInformationCommandHandler productCategoryChangeInformationCommandHandler,
                                                   ProductCategoryChangeParentCommandHandler productCategoryChangeParentCommandHandler) {
        this.productCategoryCreateCommandHandler = productCategoryCreateCommandHandler;
        this.productCategoryActivateCommandHandler = productCategoryActivateCommandHandler;
        this.productCategoryArchiveCommandHandler = productCategoryArchiveCommandHandler;
        this.productCategoryDeleteCommandHandler = productCategoryDeleteCommandHandler;
        this.productCategoryChangeImageCommandHandler = productCategoryChangeImageCommandHandler;
        this.productCategoryChangeInformationCommandHandler = productCategoryChangeInformationCommandHandler;
        this.productCategoryChangeParentCommandHandler = productCategoryChangeParentCommandHandler;
    }

    @Override
    public void create(ProductCategoryCreateRequest request) {
        var command = ProductCategoryCreateCommand.builder()
                .name(ProductCategoryName.of(request.getName()))
                .description(ProductCategoryDescription.of(request.getDescription()))
                .path(ProductCategoryPath.of(request.getName()))
                .build();
        this.productCategoryCreateCommandHandler.handle(command);
    }

    @Override
    public void activate(ProductCategoryActivateRequest request) {
        var command = ProductCategoryActivateCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .build();
        this.productCategoryActivateCommandHandler.handle(command);
    }

    @Override
    public void archive(ProductCategoryArchiveRequest request) {
        var command = ProductCategoryArchiveCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .build();
        this.productCategoryArchiveCommandHandler.handle(command);
    }

    @Override
    public void delete(ProductCategoryDeleteRequest request) {
        var command = ProductCategoryDeleteCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .build();
        this.productCategoryDeleteCommandHandler.handle(command);
    }

    @Override
    public void changeImage(ProductCategoryChangeImageRequest request) {
        var command = ProductCategoryChangeImageCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .image(request.getImage())
                .build();
        this.productCategoryChangeImageCommandHandler.handle(command);
    }

    @Override
    public void changeInformation(ProductCategoryChangeInformationRequest request) {
        var command = ProductCategoryChangeInformationCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .name(ProductCategoryName.of(request.getName()))
                .description(ProductCategoryDescription.of(request.getDescription()))
                .path(ProductCategoryPath.of(request.getName()))
                .build();
        this.productCategoryChangeInformationCommandHandler.handle(command);
    }

    @Override
    public void changeParent(ProductCategoryChangeParentRequest request) {
        var command = ProductCategoryChangeParentCommand.builder()
                .productCategoryId(ProductCategoryId.from(request.getCategoryId()))
                .parentId(ProductCategoryId.from(request.getParentId()))
                .build();
        this.productCategoryChangeParentCommandHandler.handle(command);
    }
}
