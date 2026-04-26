package az.kon.academy.catalog.command.service.domain.core.vo.management.category;

import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class ProductCategoryPath {

    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 40;

    private final String value;

    public ProductCategoryPath(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.PATH_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < ProductCategoryPath.MIN_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.PATH_TOO_SHORT);
        }

        if (normalized.length() > ProductCategoryPath.MAX_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.PATH_TOO_LONG);
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
        ProductCategoryPath that = (ProductCategoryPath) o;
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