package az.kon.academy.catalog.command.service.domain.core.vo.product;

import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class ProductSpecificationValue {
    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 20;

    private final String value;

    public static ProductSpecificationValue of(String value) {
        return new ProductSpecificationValue(value);
    }

    public ProductSpecificationValue(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.SPECIFICATION_VALUE_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < ProductName.MIN_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.SPECIFICATION_VALUE_TOO_SHORT);
        }

        if (normalized.length() > ProductName.MAX_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.SPECIFICATION_VALUE_TOO_LONG);
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
        ProductSpecificationValue that = (ProductSpecificationValue) o;
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
