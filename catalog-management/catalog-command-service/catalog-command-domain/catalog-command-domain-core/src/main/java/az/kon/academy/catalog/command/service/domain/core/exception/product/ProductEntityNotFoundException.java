package az.kon.academy.catalog.command.service.domain.core.exception.product;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogEntityNotFoundException;

import java.util.List;

public class ProductEntityNotFoundException extends CatalogEntityNotFoundException {

    public ProductEntityNotFoundException(String code) {
        super("product", code);
    }

    public ProductEntityNotFoundException(String code, Throwable cause) {
        super("product", code, cause);
    }

    public ProductEntityNotFoundException(String code, List<String> args) {
        super("product", code, args);
    }

    public ProductEntityNotFoundException(String code, List<String> args, Throwable cause) {
        super("product", code, args, cause);
    }
}