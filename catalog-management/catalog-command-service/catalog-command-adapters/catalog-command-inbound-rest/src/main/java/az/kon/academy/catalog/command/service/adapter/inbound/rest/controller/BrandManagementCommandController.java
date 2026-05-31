package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.brand.BrandManagementCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonSolveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.port.inbound.brand.BrandManagementInboundPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = "/api/v1/catalog/private/brands",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class BrandManagementCommandController implements BrandManagementCommandApiSpecification {
    private final BrandManagementInboundPort brandManagementInboundPort;

    public BrandManagementCommandController(BrandManagementInboundPort brandManagementInboundPort) {
        this.brandManagementInboundPort = brandManagementInboundPort;
    }

    @Override
    @PostMapping(path = "/create", version = "1.0")
    public ResponseEntity<BrandCreateCommandResult> create(@Valid @RequestBody BrandCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.brandManagementInboundPort.create(request));
    }

    @Override
    @PutMapping(path = "/change/image", version = "1.0")
    public ResponseEntity<Void> changeImage(@Valid @RequestBody BrandChangeImageRequest request) {
        this.brandManagementInboundPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/information", version = "1.0")
    public ResponseEntity<Void> changeInformation(@Valid @RequestBody BrandChangeInformationRequest request) {
        this.brandManagementInboundPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/draft", version = "1.0")
    public ResponseEntity<Void> moveToDraft(@Valid @RequestBody BrandMoveToDraftRequest request) {
        this.brandManagementInboundPort.moveToDraft(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/sent-to-approval", version = "1.0")
    public ResponseEntity<Void> sentToApproval(@Valid @RequestBody BrandSentToApprovalRequest request) {
        this.brandManagementInboundPort.sentToApproval(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/reject/solve", version = "1.0")
    public ResponseEntity<Void> solveRejectionReason(@Valid @RequestBody BrandRejectionReasonSolveRequest request) {
        this.brandManagementInboundPort.solveRejectionReason(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
