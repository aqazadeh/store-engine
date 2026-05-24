package az.kon.academy.catalog.command.service.adapter.inbound.rest.controller;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.BrandCommandApiSpecification;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandApproveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandChangeOwnerRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandCreateGlobalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandRejectRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.*;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import az.kon.academy.catalog.command.service.application.service.port.inbound.rest.BrandRestPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandCommandController implements BrandCommandApiSpecification {
    private final BrandRestPort brandRestPort;

    public BrandCommandController(BrandRestPort brandRestPort) {
        this.brandRestPort = brandRestPort;
    }

    @Override
    public ResponseEntity<BrandCreateCommandResult> createGlobal(BrandCreateGlobalRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.brandRestPort.createGlobal(request));
    }

    @Override
    public ResponseEntity<BrandCreateCommandResult> createMerchant(BrandCreateForMerchantRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.brandRestPort.createMerchant(request));
    }

    @Override
    public ResponseEntity<Void> approve(BrandApproveRequest request) {
        this.brandRestPort.approve(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> reject(BrandRejectRequest request) {
        this.brandRestPort.reject(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeOwner(BrandChangeOwnerRequest request) {
        this.brandRestPort.changeOwner(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeImage(BrandChangeImageRequest request) {
        this.brandRestPort.changeImage(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> changeInformation(BrandChangeInformationRequest request) {
        this.brandRestPort.changeInformation(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> moveToDraft(BrandMoveToDraftRequest request) {
        this.brandRestPort.moveToDraft(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<Void> sentToApproval(BrandSentToApprovalRequest request) {
        this.brandRestPort.sentToApproval(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
