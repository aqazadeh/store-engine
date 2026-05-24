package az.kon.academy.catalog.command.service.domain.core.exception.variant;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class VariantEntityNotFoundException extends CatalogEntityNotFoundException {

    public VariantEntityNotFoundException(String code) {
        super("variant", code);
    }

    public VariantEntityNotFoundException(String code, Throwable cause) {
        super("variant", code, cause);
    }

    public VariantEntityNotFoundException(String code, List<String> args) {
        super("variant", code, args);
    }

    public VariantEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("variant", code, args, cause);
    }
}