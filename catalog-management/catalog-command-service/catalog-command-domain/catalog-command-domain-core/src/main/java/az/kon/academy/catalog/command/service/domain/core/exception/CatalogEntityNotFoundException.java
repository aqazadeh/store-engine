package az.kon.academy.catalog.command.service.domain.core.exception;

import az.kon.academy.exception.SeEntityNotFoundException;

import java.util.List;

public class CatalogEntityNotFoundException extends SeEntityNotFoundException {
    public CatalogEntityNotFoundException(String domain, String code) {
        super("catalog", domain, code);
    }

    public CatalogEntityNotFoundException(String domain, String code, Throwable cause) {
        super("catalog", domain, code, cause);
    }

    public CatalogEntityNotFoundException(String domain, String code, List<String> args) {
        super("catalog", domain, code, args);
    }

    public CatalogEntityNotFoundException(String domain, String code, List<String> args, Throwable cause) {
        super("catalog", domain, code, args, cause);
    }
}
