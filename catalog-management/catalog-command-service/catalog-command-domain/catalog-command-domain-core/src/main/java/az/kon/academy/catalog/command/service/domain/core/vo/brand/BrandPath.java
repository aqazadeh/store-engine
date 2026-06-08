package az.kon.academy.catalog.command.service.domain.core.vo.brand;

import az.kon.academy.application.core.helper.Slugify;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainErrorCodes;
import az.kon.academy.catalog.command.service.domain.core.exception.brand.BrandDomainException;
import az.kon.academy.catalog.command.service.domain.core.exception.category.ProductCategoryDomainException;

import java.util.Objects;

public final class BrandPath {

    public static final int MIN_LENGTH = 5;
    public static final int MAX_LENGTH = 20;

    private final String value;

    public static BrandPath create(String name) {
        return new BrandPath(Slugify.of(name));
    }

    public static BrandPath of(String value){
        return new BrandPath(value);
    }

    public BrandPath(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new BrandDomainException(BrandDomainErrorCodes.PATH_REQUIRED);
        }

        String normalized = value.trim();

        if (normalized.length() < BrandPath.MIN_LENGTH) {
            throw new BrandDomainException(BrandDomainErrorCodes.PATH_TOO_SHORT);
        }

        if (normalized.length() > BrandPath.MAX_LENGTH) {
            throw new BrandDomainException(BrandDomainErrorCodes.PATH_TOO_LONG);
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
        BrandPath that = (BrandPath) o;
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