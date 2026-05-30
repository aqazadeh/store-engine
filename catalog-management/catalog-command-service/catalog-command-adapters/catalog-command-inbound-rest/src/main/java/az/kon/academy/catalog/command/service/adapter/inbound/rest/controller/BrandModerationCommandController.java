package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.brand.BrandModerationCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonAddRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonChangeReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.brand.BrandModerationPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = "/api/v1/catalog/moderation/brands",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class BrandModerationCommandController implements BrandModerationCommandApiSpecification {
    private final BrandModerationPort brandModerationPort;

    public BrandModerationCommandController(BrandModerationPort brandModerationPort) {
        this.brandModerationPort = brandModerationPort;
    }

    @Override
    @PostMapping(path = "/create", version = "1.0")
    public ResponseEntity<BrandCreateCommandResult> create(@Valid @RequestBody BrandCreateFromModerationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.brandModerationPort.create(request));
    }

    @Override
    @PutMapping(path = "/move-to-in-review", version = "1.0")
    public ResponseEntity<Void> moveToInReview(@Valid @RequestBody BrandMoveToInReviewRequest request) {
        this.brandModerationPort.moveToInReview(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/approve", version = "1.0")
    public ResponseEntity<Void> approve(@Valid @RequestBody BrandApproveRequest request) {
        this.brandModerationPort.approve(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/reject", version = "1.0")
    public ResponseEntity<Void> reject(@Valid @RequestBody BrandRejectRequest request) {
        this.brandModerationPort.reject(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PostMapping(path = "/reject/reason", version = "1.0")
    public ResponseEntity<Void> addRejectReason(@Valid @RequestBody BrandRejectionReasonAddRequest request) {
        this.brandModerationPort.addRejectReason(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Override
    @DeleteMapping(path = "/reject/reason", version = "1.0")
    public ResponseEntity<Void> removeRejectReason(@Valid @RequestBody BrandRejectionReasonDeleteRequest request) {
        this.brandModerationPort.removeRejectReason(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeReason(@Valid @RequestBody BrandRejectionReasonChangeReasonRequest request) {
        this.brandModerationPort.changeRejectionReason(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/owner", version = "1.0")
    public ResponseEntity<Void> changeOwner(@Valid @RequestBody BrandChangeOwnerRequest request) {
        this.brandModerationPort.changeOwner(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/image", version = "1.0")
    public ResponseEntity<Void> changeImage(@Valid @RequestBody BrandChangeImageRequest request) {
        this.brandModerationPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/information", version = "1.0")
    public ResponseEntity<Void> changeInformation(@Valid @RequestBody BrandChangeInformationRequest request) {
        this.brandModerationPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @PutMapping(path = "/change/global", version = "1.0")
    public ResponseEntity<Void> changeGlobal(@Valid @RequestBody BrandChangeToGlobalRequest request) {
        this.brandModerationPort.changeToGlobal(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
