package az.kon.academy.catalog.command.service.application.service.dto.request.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductSpecificationRemoveCategoryAssignmentRequest {
    private UUID specificationId;
    private UUID categoryId;
}
