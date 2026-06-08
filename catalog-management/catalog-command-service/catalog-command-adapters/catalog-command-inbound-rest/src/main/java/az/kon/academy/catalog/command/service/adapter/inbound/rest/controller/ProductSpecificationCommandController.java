package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductSpecificationCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.specification.ProductSpecificationModerationInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSpecificationCommandController implements ProductSpecificationCommandApiSpecification {

    private final ProductSpecificationModerationInboundPort productSpecificationModerationInboundPort;

    public ProductSpecificationCommandController(ProductSpecificationModerationInboundPort productSpecificationModerationInboundPort) {
        this.productSpecificationModerationInboundPort = productSpecificationModerationInboundPort;
    }

    @Override
    public ResponseEntity<Void> create(ProductSpecificationCreateRequest request) {
        this.productSpecificationModerationInboundPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeInformation(ProductSpecificationChangeInformationRequest request) {
        this.productSpecificationModerationInboundPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> delete(ProductSpecificationDeleteRequest request) {
        this.productSpecificationModerationInboundPort.delete(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> assignCategory(ProductSpecificationAssignCategoryRequest request) {
        this.productSpecificationModerationInboundPort.assignCategory(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> removeCategoryAssignment(ProductSpecificationRemoveCategoryAssignmentRequest request) {
        this.productSpecificationModerationInboundPort.removeCategoryAssignment(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
