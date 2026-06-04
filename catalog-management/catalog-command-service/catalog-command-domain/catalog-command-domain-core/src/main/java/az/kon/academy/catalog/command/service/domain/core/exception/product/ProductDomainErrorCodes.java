package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductDomainErrorCodes {

    public static final String STATUS_INVALID_FOR_APPROVAL             = "020001";
    public static final String STATUS_INVALID_FOR_MOVE_TO_DRAFT        = "020002";
    public static final String STATUS_INVALID_FOR_MOVE_TO_IN_REVIEW    = "020008";
    public static final String STATUS_INVALID_FOR_ARCHIVE              = "020003";
    public static final String CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL = "020004";
    public static final String ENTITY_NOT_FOUND                        = "020005";
    public static final String PRICE_NOT_FOUND                         = "020006";
    public static final String STOCK_NOT_FOUND                         = "020007";
}