package az.kon.academy.catalog.command.service.domain.core.exception.category;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ProductCategoryDomainErrorCodes {

    public static final String ENTITY_NOT_FOUND             = "000001";
    public static final String PARENT_NOT_FOUND             = "000002";

    public static final String NAME_REQUIRED                = "000003";
    public static final String NAME_TOO_LONG                = "000004";
    public static final String NAME_TOO_SHORT               = "000005";

    public static final String DESCRIPTION_REQUIRED         = "000006";
    public static final String DESCRIPTION_TOO_LONG         = "000007";
    public static final String DESCRIPTION_TOO_SHORT        = "000008";

    public static final String PATH_REQUIRED                = "000009";
    public static final String PATH_TOO_LONG                = "000010";
    public static final String PATH_TOO_SHORT               = "000011";

}
