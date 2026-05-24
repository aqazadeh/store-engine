package az.kon.academy.catalog.command.service.application.service.dto.request.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ProductPriceUpdateRequest {
    private UUID priceId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
