package az.kon.academy.catalog.command.service.domain.core.exception.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class ProductSpecificationEntityNotFoundException extends CatalogEntityNotFoundException {
    public ProductSpecificationEntityNotFoundException(String code) {
        super("attribute", code);
    }

    public ProductSpecificationEntityNotFoundException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public ProductSpecificationEntityNotFoundException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public ProductSpecificationEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
