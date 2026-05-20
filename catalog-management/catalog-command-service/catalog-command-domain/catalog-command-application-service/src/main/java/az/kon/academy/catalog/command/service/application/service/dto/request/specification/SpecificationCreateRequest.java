package az.kon.academy.catalog.command.service.application.service.dto.request.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecificationCreateRequest {
    private String name;
    private String description;
}
