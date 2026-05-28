package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductCategoryCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.CategoryRestPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCategoryCommandController implements ProductCategoryCommandApiSpecification {

    private final CategoryRestPort categoryRestPort;

    public ProductCategoryCommandController(CategoryRestPort categoryRestPort) {
        this.categoryRestPort = categoryRestPort;
    }

    @Override
    public ResponseEntity<Void> createProductCategory(ProductCategoryCreateRequest request) {
        this.categoryRestPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> updateProductCategory(ProductCategoryActivateRequest request) {
        this.categoryRestPort.activate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> archiveProductCategory(ProductCategoryArchiveRequest request) {
        this.categoryRestPort.archive(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryImage(ProductCategoryChangeImageRequest request) {
        this.categoryRestPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryInformation(ProductCategoryChangeInformationRequest request) {
        this.categoryRestPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeProductCategoryParent(ProductCategoryChangeParentRequest request) {
        this.categoryRestPort.changeParent(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
