package az.kon.academy.catalog.command.service.application.service.dto.request.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductCategoryCreateRequest {
    private String name;
    private String description;
}
