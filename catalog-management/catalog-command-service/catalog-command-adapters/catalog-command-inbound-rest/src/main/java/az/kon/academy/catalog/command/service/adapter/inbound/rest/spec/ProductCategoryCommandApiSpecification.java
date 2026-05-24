package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec;

import az.kon.academy.catalog.command.service.application.service.dto.request.category.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Product Category Command Controller", description = "Command operations for managing product categories")
@RequestMapping(path = "/api/v1/catalog/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductCategoryCommandApiSpecification {

    @Operation(
            summary = "Create a new product category",
            description = "Creates a new product category based on the provided request data."
    )
    @RequestBody(
            description = "Required data to create a new product category",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Product category created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/", version = "1.0")
    ResponseEntity<Void> createProductCategory(@RequestBody ProductCategoryCreateRequest request);

    @Operation(
            summary = "Activate a product category",
            description = "Transitions an existing product category to an active state."
    )
    @RequestBody(
            description = "Identifier data of the category to be activated",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "Product category activated successfully",
            useReturnTypeSchema = true
    )
    @PutMapping(path = "/activate", version = "1.0")
    ResponseEntity<Void> updateProductCategory(@RequestBody ProductCategoryActivateRequest request);

    @Operation(
            summary = "Archive a product category",
            description = "Transitions an existing product category to an archived state. Archived categories are hidden from active listings."
    )
    @RequestBody(
            description = "Identifier data of the category to be archived",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "Product category archived successfully",
            useReturnTypeSchema = true
    )
    @PutMapping(path = "/archive", version = "1.0")
    ResponseEntity<Void> archiveProductCategory(@RequestBody ProductCategoryArchiveRequest request);

    @Operation(
            summary = "Update product category image",
            description = "Replaces the current image of an existing product category with a new one."
    )
    @RequestBody(
            description = "Category ID and the new image URL or data",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "Product category image updated successfully",
            useReturnTypeSchema = true)
    @PutMapping(path = "/image", version = "1.0")
    ResponseEntity<Void> changeProductCategoryImage(@RequestBody ProductCategoryChangeImageRequest request);

    @Operation(
            operationId = "changeProductCategoryInformation",
            summary = "Update product category information",
            description = "Updates the name, description, and other details of an existing product category."
    )
    @RequestBody(
            description = "Category ID and the fields to be updated (name, description, etc.)",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(
            responseCode = "200",
            description = "Product category information updated successfully",
            useReturnTypeSchema = true
    )
    @PutMapping(path = "/information", version = "1.0")
    ResponseEntity<Void> changeProductCategoryInformation(@RequestBody ProductCategoryChangeInformationRequest request);

    @Operation(
            summary = "Change product category parent",
            description = "Moves an existing product category under a different parent category. Circular dependency validation is enforced."
    )
    @RequestBody(
            description = "Child category ID and the new parent category ID",
            required = true,
            useParameterTypeSchema = true)
    @ApiResponse(
            responseCode = "200",
            description = "Product category parent updated successfully",
            useReturnTypeSchema = true
    )
    @PutMapping(path = "/parent", version = "1.0")
    ResponseEntity<Void> changeProductCategoryParent(@RequestBody ProductCategoryChangeParentRequest request);
}