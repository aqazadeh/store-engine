package az.kon.academy.catalog.command.service.domain.core.exception.product;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductDomainException extends CatalogDomainException {

    public ProductDomainException(String code) {
        super("product", code);
    }

    public ProductDomainException(String code, Throwable cause) {
        super("product", code, cause);
    }

    public ProductDomainException(String code, List<String> args) {
        super("product", code, args);
    }

    public ProductDomainException(String code, List<String> args, Throwable cause) {
        super("product", code, args, cause);
    }
}