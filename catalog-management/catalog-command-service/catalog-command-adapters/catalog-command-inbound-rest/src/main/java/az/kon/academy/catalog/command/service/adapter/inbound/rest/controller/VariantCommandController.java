package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.VariantCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.variant.*;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.VariantRestPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VariantCommandController implements VariantCommandApiSpecification {

    private final VariantRestPort variantRestPort;

    public VariantCommandController(VariantRestPort variantRestPort) {
        this.variantRestPort = variantRestPort;
    }

    @Override
    public ResponseEntity<Void> createKey(VariantKeyCreateRequest request) {
        this.variantRestPort.createKey(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeKeyName(VariantKeyChangeNameRequest request) {
        this.variantRestPort.changeKeyName(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeKeyDescription(VariantKeyChangeDescriptionRequest request) {
        this.variantRestPort.changeKeyDescription(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> createValue(VariantValueCreateRequest request) {
        this.variantRestPort.createValue(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeValueName(VariantValueChangeNameRequest request) {
        this.variantRestPort.changeValueName(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
