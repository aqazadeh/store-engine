package az.kon.academy.catalog.command.service.domain.core.exception.product;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductDomainErrorCodes {

    public static final String STATUS_INVALID_FOR_APPROVAL             = "020001";
    public static final String STATUS_INVALID_FOR_MOVE_TO_DRAFT        = "020002";
    public static final String STATUS_INVALID_FOR_ARCHIVE              = "020003";
    public static final String CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL = "020004";
}