package az.kon.academy.catalog.command.service.adapter.inbound.rest.spec;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Product Command Controller", description = "Command operations for managing products")
@RequestMapping(path = "/api/v1/catalog/categories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public interface ProductCommandApiSpecification {
}
