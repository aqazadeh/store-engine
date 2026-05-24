package az.kon.academy.catalog.command.service.application.service.dto.request.variant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VariantValueCreateRequest {
    private UUID keyId;
    private String name;
}
