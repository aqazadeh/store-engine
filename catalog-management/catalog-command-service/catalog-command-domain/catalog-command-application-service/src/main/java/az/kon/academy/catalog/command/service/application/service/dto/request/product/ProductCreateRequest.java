package az.kon.academy.catalog.command.service.application.service.dto.request.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductCreateRequest {
    private UUID categoryId;
    private UUID brandId;
    private String name;
    private String description;
    private String barcode;
}
