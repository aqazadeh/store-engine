package az.kon.academy.catalog.command.service.application.service.dto.request.brand.management;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandChangeOwnerRequest {
    private UUID brandId;
    private UUID owner;
}
