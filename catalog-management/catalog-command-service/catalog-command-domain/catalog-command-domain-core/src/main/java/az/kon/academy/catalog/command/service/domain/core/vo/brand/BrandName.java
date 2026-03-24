package az.kon.academy.catalog.command.service.domain.core.vo.brand;

import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;

import java.util.Objects;

public final class BrandName {

    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 20;

    private final String value;

    public BrandName(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < BrandName.MIN_LENGTH) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_TOO_SHORT);
        }

        if (normalized.length() > BrandName.MAX_LENGTH) {
            throw new BrandDomainException(BrandDomainErrorCodes.NAME_TOO_LONG);
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
        BrandName that = (BrandName) o;
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