package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductCategoryCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.CategoryInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCategoryCommandController implements ProductCategoryCommandApiSpecification {

    private final CategoryInboundPort categoryInboundPort;

    public ProductCategoryCommandController(CategoryInboundPort categoryInboundPort) {
        this.categoryInboundPort = categoryInboundPort;
    }

    @Override
    public ResponseEntity<Void> createProductCategory(ProductCategoryCreateRequest request) {
        this.categoryInboundPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> updateProductCategory(ProductCategoryActivateRequest request) {
        this.categoryInboundPort.activate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> archiveProductCategory(ProductCategoryArchiveRequest request) {
        this.categoryInboundPort.archive(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryImage(ProductCategoryChangeImageRequest request) {
        this.categoryInboundPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryInformation(ProductCategoryChangeInformationRequest request) {
        this.categoryInboundPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryParent(ProductCategoryChangeParentRequest request) {
        this.categoryInboundPort.changeParent(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
