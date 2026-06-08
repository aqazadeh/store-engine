package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductPriceDomainErrorCodes {

    public static final String MIN_GREATER_THAN_MAX = "050001";
}
