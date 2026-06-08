package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductStockDomainErrorCodes {

    public static final String INSUFFICIENT_STOCK           = "030001";
    public static final String NEGATIVE_QUANTITY            = "030002";
    public static final String INSUFFICIENT_AVAILABLE_STOCK = "030003";
}