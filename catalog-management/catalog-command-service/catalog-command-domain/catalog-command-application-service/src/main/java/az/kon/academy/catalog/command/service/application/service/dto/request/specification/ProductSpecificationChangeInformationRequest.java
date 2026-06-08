package az.kon.academy.catalog.command.service.application.service.dto.request.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductSpecificationChangeInformationRequest {
    private UUID specificationId;
    private String name;
    private String description;
}
