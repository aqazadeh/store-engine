package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductVariantDomainErrorCodes {

    public static final String ONLY_DRAFT_CAN_BE_REMOVED  = "040001";
    public static final String ONLY_DRAFT_CAN_BE_CHANGED  = "040002";
}
