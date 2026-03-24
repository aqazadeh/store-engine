package az.kon.academy.catalog.command.service.domain.core.exception.attribute;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class ProductAttributeEntityNotFoundException extends CatalogEntityNotFoundException {
    public ProductAttributeEntityNotFoundException(String code) {
        super("attribute", code);
    }

    public ProductAttributeEntityNotFoundException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public ProductAttributeEntityNotFoundException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public ProductAttributeEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
