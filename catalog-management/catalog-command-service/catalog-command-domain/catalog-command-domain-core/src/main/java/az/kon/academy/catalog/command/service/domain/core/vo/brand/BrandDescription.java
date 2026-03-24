package az.kon.academy.catalog.command.service.domain.core.vo.brand;

import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class BrandDescription {

    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 150;

    private final String value;

    public BrandDescription(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(BrandDomainErrorCodes.DESCRIPTION_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < BrandDescription.MIN_LENGTH) {
            throw new ProductCategoryDomainException(BrandDomainErrorCodes.DESCRIPTION_TOO_SHORT);
        }

        if (normalized.length() > BrandDescription.MAX_LENGTH) {
            throw new ProductCategoryDomainException(BrandDomainErrorCodes.DESCRIPTION_TOO_LONG);
        }

        this.value = normalized;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandDescription that = (BrandDescription) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public String toString() {
        return value;
    }
}