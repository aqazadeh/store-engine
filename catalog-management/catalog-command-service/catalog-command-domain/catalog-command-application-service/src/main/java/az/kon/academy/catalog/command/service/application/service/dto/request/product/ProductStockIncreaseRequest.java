package az.kon.academy.catalog.command.service.application.service.dto.request.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductStockIncreaseRequest {
    private UUID stockId;
    private Integer quantity;
}
