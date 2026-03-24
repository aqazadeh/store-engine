package az.kon.academy.catalog.command.service.domain.core.vo.attribute;

import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class ProductAttributeDescription {

    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 255;

    private final String value;

    public ProductAttributeDescription(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.DESCRIPTION_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < ProductAttributeDescription.MIN_LENGTH) {
            throw new ProductCategoryDomainException(ProductCategoryDomainErrorCodes.DESCRIPTION_TOO_SHORT);
        }

        if (normalized.length() > ProductAttributeDescription.MAX_LENGTH) {
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
        ProductAttributeDescription that = (ProductAttributeDescription) o;
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