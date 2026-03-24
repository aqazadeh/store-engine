package az.kon.academy.catalog.command.service.domain.core.exception.brand;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrandDomainErrorCodes {

    public static final String ENTITY_NOT_FOUND                         = "000001";
    public static final String NAME_ALREADY_EXISTS                      = "000002";

    public static final String NAME_REQUIRED                            = "000003";
    public static final String NAME_TOO_LONG                            = "000004";
    public static final String NAME_TOO_SHORT                           = "000005";

    public static final String DESCRIPTION_REQUIRED                     = "000006";
    public static final String DESCRIPTION_TOO_LONG                     = "000007";
    public static final String DESCRIPTION_TOO_SHORT                    = "000008";

    public static final String PATH_REQUIRED                            =  "000006";
    public static final String PATH_TOO_LONG                            =  "000007";
    public static final String PATH_TOO_SHORT                           =  "000008";

    public static final String STATUS_INVALID_FOR_APPROVAL              =  "000009";
    public static final String TOO_MANY_BRANDS_FOR_MERCHANT             =  "000010";
    public static final String CANNOT_BE_CHANGED_WHEN_SENT_TO_APPROVAL  = "000011";
    public static final String STATUS_INVALID_FOR_MOVE_TO_DRAFT         = "000012";
}
