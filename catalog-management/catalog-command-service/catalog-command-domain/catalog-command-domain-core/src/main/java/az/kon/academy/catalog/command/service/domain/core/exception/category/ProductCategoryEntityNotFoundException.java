package az.kon.academy.catalog.command.service.domain.core.exception.category;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class ProductCategoryEntityNotFoundException extends CatalogEntityNotFoundException {
    public ProductCategoryEntityNotFoundException(String code) {
        super("catalog", code);
    }

    public ProductCategoryEntityNotFoundException(String code, Throwable cause) {
        super("catalog", code, cause);
    }

    public ProductCategoryEntityNotFoundException(String code, List<String> args) {
        super("catalog", code, args);
    }

    public ProductCategoryEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("catalog", code, args, cause);
    }
}
