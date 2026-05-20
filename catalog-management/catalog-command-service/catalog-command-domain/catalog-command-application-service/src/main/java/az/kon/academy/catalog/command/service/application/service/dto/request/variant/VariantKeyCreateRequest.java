package az.kon.academy.catalog.command.service.application.service.dto.request.variant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VariantKeyCreateRequest {
    private String name;
    private String description;
}
