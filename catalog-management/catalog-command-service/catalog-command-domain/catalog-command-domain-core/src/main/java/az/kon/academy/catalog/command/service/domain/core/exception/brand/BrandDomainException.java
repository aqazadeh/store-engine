package az.kon.academy.catalog.command.service.domain.core.exception.brand;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class BrandDomainException extends CatalogDomainException {
    public BrandDomainException(String code) {
        super("brand", code);
    }

    public BrandDomainException(String code, Throwable cause) {
        super("brand", code, cause);
    }

    public BrandDomainException(String code, List<String> args) {
        super("brand", code, args);
    }

    public BrandDomainException(String code, List<String> args, Throwable cause) {
        super("brand", code, args, cause);
    }
}
