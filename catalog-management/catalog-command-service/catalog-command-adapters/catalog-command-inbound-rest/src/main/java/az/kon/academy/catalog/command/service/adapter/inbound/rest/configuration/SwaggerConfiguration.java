package az.kon.academy.catalog.command.service.adapter.inbound.rest.configuration;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.ErrorResponse;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    ResolvedSchema resolvedSchema = ModelConverters.getInstance()
            .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class));

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("ErrorResponse", resolvedSchema.schema)
                        .addResponses("400", errorResponse("Invalid request payload"))
                        .addResponses("404", errorResponse("Not found"))
                        .addResponses("422", errorResponse("Invalid payload"))
                        .addResponses("500", errorResponse("Internal server error"))
                );
    }

    @Bean
    public OperationCustomizer globalResponses() {
        return (operation, handlerMethod) -> {
            operation.getResponses()
                    .addApiResponse("400", refResponse("400"))
                    .addApiResponse("404", refResponse("404"))
                    .addApiResponse("422", refResponse("422"))
                    .addApiResponse("500", refResponse("500"));
            return operation;
        };
    }

    private ApiResponse errorResponse(String description) {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("application/json",
                                new MediaType().schema(
                                        new Schema<>().$ref("#/components/schemas/ErrorResponse")
                                )));
    }

    private ApiResponse refResponse(String code) {
        return new ApiResponse()
                .$ref("#/components/responses/" + code);
    }
}
