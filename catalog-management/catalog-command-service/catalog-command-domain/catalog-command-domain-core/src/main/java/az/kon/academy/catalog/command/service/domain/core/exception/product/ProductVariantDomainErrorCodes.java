package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductVariantDomainErrorCodes {

    public static final String ONLY_DRAFT_CAN_BE_REMOVED    = "040001";
    public static final String ONLY_DRAFT_CAN_BE_CHANGED    = "040002";
    public static final String STATUS_INVALID_FOR_ACTIVATE  = "040003";
    public static final String STATUS_INVALID_FOR_DEACTIVATE = "040004";
    public static final String STATUS_INVALID_FOR_ARCHIVE   = "040005";
    public static final String STATUS_INVALID_FOR_DISCONTINUE = "040006";
    public static final String STATUS_INVALID_FOR_OUT_OF_STOCK = "040007";
    public static final String INVALID_VARIANT_ASSIGNMENT    = "040008";
}
