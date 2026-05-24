package az.kon.academy.catalog.command.service.application.service.dto.request.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductAssignCategoryRequest {
    private UUID productId;
    private UUID categoryId;
}
