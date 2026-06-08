package az.kon.academy.catalog.command.service.domain.core.vo;

import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.product.ProductDomainException;

import java.util.Objects;

public final class Barcode {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 14;

    private final String value;

    private Barcode(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ProductDomainException(ProductDomainErrorCodes.BARCODE_REQUIRED);
        }

        var normalized = value.trim();

        if (!normalized.chars().allMatch(Character::isDigit)) {
            throw new ProductDomainException(ProductDomainErrorCodes.BARCODE_INVALID_FORMAT);
        }

        if (normalized.length() < MIN_LENGTH) {
            throw new ProductDomainException(ProductDomainErrorCodes.BARCODE_TOO_SHORT);
        }

        if (normalized.length() > MAX_LENGTH) {
            throw new ProductDomainException(ProductDomainErrorCodes.BARCODE_TOO_LONG);
        }

        this.value = normalized;
    }

    public static Barcode of(String value) {
        return new Barcode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barcode that = (Barcode) o;
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