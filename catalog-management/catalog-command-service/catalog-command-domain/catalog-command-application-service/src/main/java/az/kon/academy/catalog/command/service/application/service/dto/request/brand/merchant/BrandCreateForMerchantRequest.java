package az.kon.academy.catalog.command.service.application.service.dto.request.brand.merchant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BrandCreateForMerchantRequest {
    private String name;
    private String description;
}
