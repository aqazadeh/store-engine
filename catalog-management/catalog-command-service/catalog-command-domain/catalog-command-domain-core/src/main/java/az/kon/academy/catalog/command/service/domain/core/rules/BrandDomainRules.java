package az.kon.academy.catalog.command.service.domain.core.rules;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BrandDomainRules {
    public static final Integer MAX_BRANDS_PER_MERCHANT = 3;
}
