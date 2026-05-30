package az.kon.academy.catalog.command.service.application.service.dto.request.brand;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandMoveToDraftRequest {
    private UUID brandId;
}
