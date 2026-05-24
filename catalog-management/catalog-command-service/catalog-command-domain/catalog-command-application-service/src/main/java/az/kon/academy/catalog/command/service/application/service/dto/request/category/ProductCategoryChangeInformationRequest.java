package az.kon.academy.catalog.command.service.application.service.dto.request.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductCategoryChangeInformationRequest {
    private UUID categoryId;
    private String name;
    private String description;
}
