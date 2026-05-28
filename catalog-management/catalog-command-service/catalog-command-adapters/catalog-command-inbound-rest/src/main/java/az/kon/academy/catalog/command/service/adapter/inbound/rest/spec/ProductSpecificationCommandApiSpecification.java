package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec;

import az.kon.academy.catalog.command.service.application.service.dto.request.specification.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product Specification Command Controller", description = "Command operations for managing product specifications")
@RequestMapping(path = "/api/v1/catalog/specification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductSpecificationCommandApiSpecification {

    @Operation(
            summary = "Create a new product specification",
            description = "Creates a new product specification that can later be assigned to product categories."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required data to create a new product specification",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Product specification created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/create", version = "1.0")
    ResponseEntity<Void> create(@Valid @RequestBody SpecificationCreateRequest request);

    @Operation(
            summary = "Change product specification information",
            description = "Updates the name and description of an existing product specification."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Specification identifier and the new name and description values",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Product specification information updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/change/information", version = "1.0")
    ResponseEntity<Void> changeInformation(@Valid @RequestBody SpecificationChangeInformationRequest request);

    @Operation(
            summary = "Delete a product specification",
            description = "Permanently deletes an existing product specification. All category assignments for this specification will also be removed."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Identifier of the product specification to be deleted",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Product specification deleted successfully", useReturnTypeSchema = true)
    @DeleteMapping(path = "/remove", version = "1.0")
    ResponseEntity<Void> delete(@Valid @RequestBody SpecificationDeleteRequest request);

    @Operation(
            summary = "Assign specification to a category",
            description = "Links a product specification to a product category, optionally marking it as required for products in that category."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Specification identifier, category identifier, and whether the specification is required",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Specification assigned to category successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/assign/category", version = "1.0")
    ResponseEntity<Void> assignCategory(@Valid @RequestBody SpecificationAssignCategoryRequest request);

    @Operation(
            summary = "Remove specification from a category",
            description = "Unlinks a product specification from a product category."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Specification identifier and category identifier to remove the assignment",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Specification removed from category successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/remove/category-assignment", version = "1.0")
    ResponseEntity<Void> removeCategoryAssignment(@Valid @RequestBody SpecificationRemoveCategoryAssignmentRequest request);
}
