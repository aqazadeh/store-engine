package az.kon.academy.catalog.command.service.domain.core.vo.management.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class SpecificationDescription {

    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 255;

    private final String value;

    public SpecificationDescription(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.DESCRIPTION_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < SpecificationDescription.MIN_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.DESCRIPTION_TOO_SHORT);
        }

        if (normalized.length() > SpecificationDescription.MAX_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.DESCRIPTION_TOO_LONG);
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
        SpecificationDescription that = (SpecificationDescription) o;
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