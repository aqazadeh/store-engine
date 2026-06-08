package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.brand;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonSolveRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Brand Management Command Controller", description = "Command operations for managing brands")
public interface BrandManagementCommandApiSpecification {

    @Operation(summary = "Create a new merchant brand", description = "Creates a new brand for the authenticated merchant. A merchant may not exceed the maximum allowed brand count.")
    @RequestBody(description = "Required data to create a new Merchant Brand", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "201", description = "Brand created successfully", useReturnTypeSchema = true)
    ResponseEntity<BrandCreateCommandResult> create(BrandCreateRequest request);

    @Operation(summary = "Change brand image", description = "Replaces the current image of an existing brand with a new one.")
    @RequestBody(description = "Brand identifier and the new image URL", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand image updated successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeImage(BrandChangeImageRequest request);

    @Operation(summary = "Change brand information", description = "Updates the name and description of an existing brand. Cannot be changed while the brand is in 'sent to approval' state.")
    @RequestBody(description = "Brand identifier and the new name and description values", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand information updated successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeInformation(BrandChangeInformationRequest request);

    @Operation(summary = "Move brand to draft", description = "Transitions a brand from 'sent to approval' back to 'draft' state, allowing further edits.")
    @RequestBody(description = "Identifier of the brand to be moved to draft", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand moved to draft successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> moveToDraft(BrandMoveToDraftRequest request);

    @Operation(summary = "Send brand to approval", description = "Transitions a brand from 'draft' or 'rejected' state to 'sent to approval', initiating the moderation workflow.")
    @RequestBody(description = "Identifier of the brand to be sent to approval", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand sent to approval successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> sentToApproval(BrandSentToApprovalRequest request);

    @Operation(summary = "Resolve a rejection reason", description = "Marks a rejection reason as resolved after the required corrections have been completed.")
    @RequestBody(description = "Identifier of the rejection reason to be resolved", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Rejection reason resolved successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> solveRejectionReason(BrandRejectionReasonSolveRequest request);
}
