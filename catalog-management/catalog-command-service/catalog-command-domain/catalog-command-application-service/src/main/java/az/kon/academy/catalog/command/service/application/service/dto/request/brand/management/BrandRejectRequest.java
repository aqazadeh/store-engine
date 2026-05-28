package az.kon.academy.catalog.command.service.application.service.dto.request.brand.management;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BrandRejectRequest {
    private UUID brandId;
    private String reason;
}
