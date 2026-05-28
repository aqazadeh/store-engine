package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec;

import az.kon.academy.catalog.command.service.application.service.dto.request.variant.*;
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

@Tag(name = "Variant Command Controller", description = "Command operations for managing variants")
@RequestMapping(path = "/api/v1/catalog/variants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface VariantCommandApiSpecification {

    @Operation(
            summary = "Create a new variant key",
            description = "Creates a new variant key (e.g. Color, Size) that can be used to define product variant options."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required data to create a new variant key",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Variant key created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/key/create", version = "1.0")
    ResponseEntity<Void> createKey(@Valid @RequestBody VariantKeyCreateRequest request);

    @Operation(
            summary = "Change variant key name",
            description = "Updates the name of an existing variant key."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Variant key identifier and the new name value",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Variant key name updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/key/change/name", version = "1.0")
    ResponseEntity<Void> changeKeyName(@Valid @RequestBody VariantKeyChangeNameRequest request);

    @Operation(
            summary = "Change variant key description",
            description = "Updates the description of an existing variant key."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Variant key identifier and the new description value",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Variant key description updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/key/change/description", version = "1.0")
    ResponseEntity<Void> changeKeyDescription(@Valid @RequestBody VariantKeyChangeDescriptionRequest request);

    @Operation(
            summary = "Create a new variant value",
            description = "Creates a new variant value under an existing variant key (e.g. Red under Color, XL under Size)."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required data to create a new variant value, including the parent key identifier",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "201", description = "Variant value created successfully", useReturnTypeSchema = true)
    @PostMapping(path = "/value/create", version = "1.0")
    ResponseEntity<Void> createValue(@Valid @RequestBody VariantValueCreateRequest request);

    @Operation(
            summary = "Change variant value name",
            description = "Updates the name of an existing variant value."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Variant value identifier and the new name value",
            required = true,
            useParameterTypeSchema = true
    )
    @ApiResponse(responseCode = "200", description = "Variant value name updated successfully", useReturnTypeSchema = true)
    @PutMapping(path = "/value/change/name", version = "1.0")
    ResponseEntity<Void> changeValueName(@Valid @RequestBody VariantValueChangeNameRequest request);
}
