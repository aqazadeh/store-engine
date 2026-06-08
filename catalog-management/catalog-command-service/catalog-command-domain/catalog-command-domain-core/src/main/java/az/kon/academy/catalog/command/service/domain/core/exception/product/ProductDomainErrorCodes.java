package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductDomainErrorCodes {

    public static final String STATUS_INVALID_FOR_APPROVAL             = "000001";
    public static final String STATUS_INVALID_FOR_MOVE_TO_DRAFT        = "000002";
    public static final String STATUS_INVALID_FOR_MOVE_TO_IN_REVIEW    = "000008";
    public static final String STATUS_INVALID_FOR_ARCHIVE              = "000003";
    public static final String CANNOT_BE_CHANGED_WHEN_IN_REVIEW        = "000004";
    public static final String ENTITY_NOT_FOUND                        = "000005";
    public static final String PRICE_NOT_FOUND                         = "000006";
    public static final String STOCK_NOT_FOUND                         = "000007";

    public static final String CATEGORY_NOT_FOUND                      = "000008";
    public static final String BRAND_NOT_FOUND                         = "000009";

    public static final String BARCODE_REQUIRED                        = "000010";
    public static final String BARCODE_INVALID_FORMAT                  = "000011";
    public static final String BARCODE_TOO_SHORT                       = "000012";
    public static final String BARCODE_TOO_LONG                        = "000013";
    public static final String STATUS_INVALID_FOR_APPROVE              = "000014";
    public static final String STATUS_INVALID_FOR_REJECT               = "000015";
    public static final String CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL  = "000016";
    public static final String REQUIRED_SPECIFICATION_MISSING           = "000017";
    public static final String STATUS_INVALID_FOR_REJECTION_OPERATION   = "000018";
}