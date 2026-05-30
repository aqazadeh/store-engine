package az.kon.academy.catalog.command.service.domain.core.exception.brand;

public interface BrandDomainErrorCodes {

    String ENTITY_NOT_FOUND                         = "000001";
    String NAME_ALREADY_EXISTS                      = "000002";

    String NAME_REQUIRED                            = "000003";
    String NAME_TOO_LONG                            = "000004";
    String NAME_TOO_SHORT                           = "000005";

    String DESCRIPTION_REQUIRED                     = "000006";
    String DESCRIPTION_TOO_LONG                     = "000007";
    String DESCRIPTION_TOO_SHORT                    = "000008";

    String PATH_REQUIRED                            = "000006";
    String PATH_TOO_LONG                            = "000007";
    String PATH_TOO_SHORT                           = "000008";

    String STATUS_INVALID_FOR_APPROVAL              = "000009";
    String TOO_MANY_BRANDS_FOR_MERCHANT             = "000010";
    String STATUS_INVALID_FOR_SENT_TO_APPROVAL      = "000011";
    String STATUS_INVALID_FOR_MOVE_TO_DRAFT         = "000012";
    String STATUS_INVALID_FOR_MOVE_TO_IN_REVIEW     = "000013";
    String STATUS_INVALID_FOR_APPROVE               = "000014";
    String STATUS_INVALID_FOR_REJECT                = "000015";
    String MERCHANT_NOT_FOUND                       = "000016";

    String HAS_UNSOLVED_REASON                      = "000017";
    String AT_LEAST_ONE_REJECTION_REASON_REQUIRED   = "000018";
}
