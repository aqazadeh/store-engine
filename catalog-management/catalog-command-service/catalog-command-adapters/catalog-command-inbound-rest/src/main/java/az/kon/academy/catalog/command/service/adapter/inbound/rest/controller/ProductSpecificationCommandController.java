package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductSpecificationCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.specification.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.SpecificationInboundPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSpecificationCommandController implements ProductSpecificationCommandApiSpecification {

    private final SpecificationInboundPort specificationInboundPort;

    public ProductSpecificationCommandController(SpecificationInboundPort specificationInboundPort) {
        this.specificationInboundPort = specificationInboundPort;
    }

    @Override
    public ResponseEntity<Void> create(SpecificationCreateRequest request) {
        this.specificationInboundPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeInformation(SpecificationChangeInformationRequest request) {
        this.specificationInboundPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> delete(SpecificationDeleteRequest request) {
        this.specificationInboundPort.delete(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> assignCategory(SpecificationAssignCategoryRequest request) {
        this.specificationInboundPort.assignCategory(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> removeCategoryAssignment(SpecificationRemoveCategoryAssignmentRequest request) {
        this.specificationInboundPort.removeCategoryAssignment(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
