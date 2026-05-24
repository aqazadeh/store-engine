package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductSpecificationCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.SpecificationRestPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSpecificationCommandController implements ProductSpecificationCommandApiSpecification {

    private final SpecificationRestPort specificationRestPort;

    public ProductSpecificationCommandController(SpecificationRestPort specificationRestPort) {
        this.specificationRestPort = specificationRestPort;
    }

    @Override
    public ResponseEntity<Void> create(SpecificationCreateRequest request) {
        this.specificationRestPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeInformation(SpecificationChangeInformationRequest request) {
        this.specificationRestPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> delete(SpecificationDeleteRequest request) {
        this.specificationRestPort.delete(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> assignCategory(SpecificationAssignCategoryRequest request) {
        this.specificationRestPort.assignCategory(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> removeCategoryAssignment(SpecificationRemoveCategoryAssignmentRequest request) {
        this.specificationRestPort.removeCategoryAssignment(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
