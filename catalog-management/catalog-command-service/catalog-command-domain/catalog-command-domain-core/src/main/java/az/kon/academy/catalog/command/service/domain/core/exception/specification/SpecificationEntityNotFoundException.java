package az.kon.academy.catalog.command.service.domain.core.exception.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class SpecificationEntityNotFoundException extends CatalogEntityNotFoundException {
    public SpecificationEntityNotFoundException(String code) {
        super("attribute", code);
    }

    public SpecificationEntityNotFoundException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public SpecificationEntityNotFoundException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public SpecificationEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
