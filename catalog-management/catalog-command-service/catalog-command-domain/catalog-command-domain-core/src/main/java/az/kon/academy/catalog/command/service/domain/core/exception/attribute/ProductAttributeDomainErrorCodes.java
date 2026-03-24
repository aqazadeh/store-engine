package az.kon.academy.catalog.command.service.domain.core.exception.attribute;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductAttributeDomainErrorCodes {

    public static final String ENTITY_NOT_FOUND                         = "000001";
    public static final String CATEGORY_NOT_FOUND                       = "000002";
}
