package az.kon.academy.catalog.command.service.domain.core.exception.brand;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class BrandRejectionDomainException extends CatalogDomainException {
    public BrandRejectionDomainException(String code) {
        super("brand-rejection-reason", code);
    }

    public BrandRejectionDomainException(String code, Throwable cause) {
        super("brand-rejection-reason", code, cause);
    }

    public BrandRejectionDomainException(String code, List<String> args) {
        super("brand-rejection-reason", code, args);
    }

    public BrandRejectionDomainException(String code, List<String> args, Throwable cause) {
        super("brand-rejection-reason", code, args, cause);
    }
}
