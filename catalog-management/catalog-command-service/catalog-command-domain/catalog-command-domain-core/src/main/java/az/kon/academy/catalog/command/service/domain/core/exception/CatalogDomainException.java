package az.kon.academy.catalog.command.service.domain.core.exception;

import az.kon.academy.exception.SeDomainException;

import java.util.List;

public class CatalogDomainException extends SeDomainException {
    public CatalogDomainException(String domain, String code) {
        super("catalog", domain, code);
    }

    public CatalogDomainException(String domain, String code, Throwable cause) {
        super("catalog", domain, code, cause);
    }

    public CatalogDomainException(String domain, String code, List<String> args) {
        super("catalog", domain, code, args);
    }

    public CatalogDomainException(String domain, String code, List<String> args, Throwable cause) {
        super("catalog", domain, code, args, cause);
    }
}
