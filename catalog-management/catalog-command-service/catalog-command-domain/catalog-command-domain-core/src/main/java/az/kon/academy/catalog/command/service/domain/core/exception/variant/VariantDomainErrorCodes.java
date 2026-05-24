package az.kon.academy.catalog.command.service.domain.core.exception.variant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VariantDomainErrorCodes {

    public static final String KEY_NOT_FOUND   = "040001";
    public static final String VALUE_NOT_FOUND = "040002";
}