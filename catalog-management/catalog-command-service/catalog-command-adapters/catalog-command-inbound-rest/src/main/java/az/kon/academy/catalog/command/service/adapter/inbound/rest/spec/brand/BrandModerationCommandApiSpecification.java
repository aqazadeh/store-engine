package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec.brand;

import az.kon.academy.catalog.command.service.application.service.dto.request.brand.*;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeImageRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brand.BrandChangeInformationRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonAddRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonChangeReasonRequest;
import az.kon.academy.catalog.command.service.application.service.dto.request.brandrejection.BrandRejectionReasonDeleteRequest;
import az.kon.academy.catalog.command.service.application.service.dto.result.BrandCreateCommandResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "Brand Moderation Command Controller", description = "Command operations for moderating brands")
public interface BrandModerationCommandApiSpecification {
    @Operation(summary = "Create a new global brand", description = "Creates a new global brand based on the provided request data.")
    @RequestBody(description = "Required data to create a new Global Brand", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "201", description = "Brand created successfully", useReturnTypeSchema = true)
    ResponseEntity<BrandCreateCommandResult> create(BrandCreateFromModerationRequest request);

    @Operation(summary = "Move a brand to review", description = "Moves a brand to the 'in review' state so it can be evaluated by moderators.")
    @RequestBody(description = "Identifier of the brand to be moved to the review state", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand moved to review successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> moveToInReview(BrandMoveToInReviewRequest request);
    
    @Operation(summary = "Approve a brand", description = "Approves a brand that is in the 'sent to approval' state. Only available to domain moderators.")
    @RequestBody(description = "Identifier of the brand to be approved", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand approved successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> approve(BrandApproveRequest request);

    @Operation(summary = "Reject a brand", description = "Rejects a brand that is in the 'sent to approval' state. Only available to domain moderators.")
    @RequestBody(description = "Identifier of the brand to be rejected", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand rejected successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> reject(BrandRejectRequest request);

    @Operation(summary = "Add a rejection reason", description = "Adds a new rejection reason that can be used by moderators when rejecting brands.")
    @RequestBody(description = "Details of the rejection reason to be created", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Rejection reason added successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> addRejectReason(BrandRejectionReasonAddRequest request);

    @Operation(summary = "Remove a rejection reason", description = "Removes an existing rejection reason. The reason must not be in active use by any pending moderation process.")
    @RequestBody(description = "Identifier of the rejection reason to be removed", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Rejection reason removed successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> removeRejectReason(BrandRejectionReasonDeleteRequest request);

    @Operation(summary = "Update a rejection reason", description = "Updates the name or details of an existing rejection reason. Only available to domain moderators.")
    @RequestBody(description = "Identifier of the rejection reason and the updated values", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Rejection reason updated successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeReason(BrandRejectionReasonChangeReasonRequest request);

    @Operation(summary = "Change brand owner", description = "Transfers ownership of a brand to a different merchant. Pass null owner to make the brand global.")
    @RequestBody(description = "Brand identifier and the new owner merchant identifier", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand owner changed successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeOwner(BrandChangeOwnerRequest request);

    @Operation(summary = "Change brand image", description = "Replaces the current image of an existing brand with a new one.")
    @RequestBody(description = "Brand identifier and the new image URL", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand image updated successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeImage(BrandChangeImageRequest request);

    @Operation(summary = "Change brand information", description = "Updates the name and description of an existing brand. Cannot be changed while the brand is in 'sent to approval' state.")
    @RequestBody(description = "Brand identifier and the new name and description values", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand information updated successfully", useReturnTypeSchema = true)
    ResponseEntity<Void> changeInformation(BrandChangeInformationRequest request);

    @Operation(summary = "Convert brand to global", description = "Converts an existing brand into a global brand. This operation can only be performed if the brand meets the required eligibility criteria.")
    @RequestBody(description = "Brand identifier and the information required to convert the brand into a global brand", required = true, useParameterTypeSchema = true)
    @ApiResponse(responseCode = "200", description = "Brand successfully converted to a global brand", useReturnTypeSchema = true)
    ResponseEntity<Void> changeGlobal(BrandChangeToGlobalRequest request);

}
