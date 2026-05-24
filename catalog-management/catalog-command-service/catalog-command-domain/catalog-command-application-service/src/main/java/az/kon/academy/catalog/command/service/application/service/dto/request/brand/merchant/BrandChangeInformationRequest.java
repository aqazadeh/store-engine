package az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandChangeInformationRequest {
    private UUID brandId;
    private String name;
    private String description;
}
