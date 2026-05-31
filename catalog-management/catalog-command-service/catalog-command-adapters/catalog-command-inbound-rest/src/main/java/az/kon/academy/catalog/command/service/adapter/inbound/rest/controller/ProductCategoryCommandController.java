package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.ProductCategoryCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.category.CategoryInboundPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductCategoryCommandController implements ProductCategoryCommandApiSpecification {

    private final CategoryInboundPort categoryInboundPort;

    public ProductCategoryCommandController(CategoryInboundPort categoryInboundPort) {
        this.categoryInboundPort = categoryInboundPort;
    }

    @Override
    @PostMapping(path = "/", version = "1.0")
    public ResponseEntity<Void> create(@Valid @RequestBody ProductCategoryCreateRequest request) {
        this.categoryInboundPort.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    @PutMapping(path = "/activate", version = "1.0")
    public ResponseEntity<Void> activate(@Valid @RequestBody ProductCategoryActivateRequest request) {
        this.categoryInboundPort.activate(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/archive", version = "1.0")
    public ResponseEntity<Void> archive(@Valid @RequestBody ProductCategoryArchiveRequest request) {
        this.categoryInboundPort.archive(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @DeleteMapping(path = "/", version = "1.0")
    public ResponseEntity<Void> delete(@Valid @RequestBody ProductCategoryDeleteRequest request) {
        this.categoryInboundPort.delete(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/image", version = "1.0")
    public ResponseEntity<Void> changeImage(@Valid @RequestBody ProductCategoryChangeImageRequest request) {
        this.categoryInboundPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/information", version = "1.0")
    public ResponseEntity<Void> changeInformation(@Valid @RequestBody ProductCategoryChangeInformationRequest request) {
        this.categoryInboundPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/parent", version = "1.0")
    public ResponseEntity<Void> changeParent(@Valid @RequestBody ProductCategoryChangeParentRequest request) {
        this.categoryInboundPort.changeParent(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
