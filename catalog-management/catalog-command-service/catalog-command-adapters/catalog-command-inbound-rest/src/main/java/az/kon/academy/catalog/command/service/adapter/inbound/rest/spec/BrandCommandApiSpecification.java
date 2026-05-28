package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandApproveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandChangeOwnerRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandCreateGlobalRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.management.BrandRejectRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant.*;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Tag(name = "Brand Command Controller", description = "Command operations for managing brands")
@RequestMapping(path = "/api/v1/catalog/brands", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface BrandCommandApiSpecification {
    @Operation(
            summary = "Create a new global brand",
            description = "Creates a new global brand based on the provided request data."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required data to create a new Global Brand",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Brand created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/create/global", version = "1.0")
    ResponseEntity<BrandCreateCommandResult> createGlobal(@Valid @RequestBody BrandCreateGlobalRequest request);

    @Operation(
            summary = "Create a new merchant brand",
            description = "Creates a new brand for the authenticated merchant. A merchant may not exceed the maximum allowed brand count."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required data to create a new Merchant Brand",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Brand created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/create", version = "1.0")
    ResponseEntity<BrandCreateCommandResult> createMerchant(@Valid @RequestBody BrandCreateForMerchantRequest request);

    @Operation(
            summary = "Approve a brand",
            description = "Approves a brand that is in the 'sent to approval' state. Only available to domain moderators."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifier of the brand to be approved",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand approved successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/approve", version = "1.0")
    ResponseEntity<Void> approve(@Valid @RequestBody BrandApproveRequest request);

    @Operation(
            summary = "Reject a brand",
            description = "Rejects a brand that is in the 'sent to approval' state. Only available to domain moderators."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifier of the brand to be rejected",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand rejected successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/reject", version = "1.0")
    ResponseEntity<Void> reject(@Valid @RequestBody BrandRejectRequest request);

    @Operation(
            summary = "Change brand owner",
            description = "Transfers ownership of a brand to a different merchant. Pass null owner to make the brand global."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Brand identifier and the new owner merchant identifier",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand owner changed successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/owner", version = "1.0")
    ResponseEntity<Void> changeOwner(@Valid @RequestBody BrandChangeOwnerRequest request);

    @Operation(
            summary = "Change brand image",
            description = "Replaces the current image of an existing brand with a new one."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Brand identifier and the new image URL",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand image updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/image", version = "1.0")
    ResponseEntity<Void> changeImage(@Valid @RequestBody BrandChangeImageRequest request);

    @Operation(
            summary = "Change brand information",
            description = "Updates the name and description of an existing brand. Cannot be changed while the brand is in 'sent to approval' state."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Brand identifier and the new name and description values",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand information updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/information", version = "1.0")
    ResponseEntity<Void> changeInformation(@Valid @RequestBody BrandChangeInformationRequest request);

    @Operation(
            summary = "Move brand to draft",
            description = "Transitions a brand from 'sent to approval' back to 'draft' state, allowing further edits."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifier of the brand to be moved to draft",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand moved to draft successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/draft", version = "1.0")
    ResponseEntity<Void> moveToDraft(@Valid @RequestBody BrandMoveToDraftRequest request);

    @Operation(
            summary = "Send brand to approval",
            description = "Transitions a brand from 'draft' or 'rejected' state to 'sent to approval', initiating the moderation workflow."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifier of the brand to be sent to approval",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Brand sent to approval successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/sent-to-approval", version = "1.0")
    ResponseEntity<Void> sentToApproval(@Valid @RequestBody BrandSentToApprovalRequest request);
}
