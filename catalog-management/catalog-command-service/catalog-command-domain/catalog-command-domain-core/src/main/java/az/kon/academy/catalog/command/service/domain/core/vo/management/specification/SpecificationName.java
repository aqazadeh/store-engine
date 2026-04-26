package az.kon.academy.catalog.command.service.domain.core.vo.management.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class SpecificationName {

    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 20;

    private final String value;

    public SpecificationName(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.NAME_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < SpecificationName.MIN_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.NAME_TOO_SHORT);
        }

        if (normalized.length() > SpecificationName.MAX_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.NAME_TOO_LONG);
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
        SpecificationName that = (SpecificationName) o;
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