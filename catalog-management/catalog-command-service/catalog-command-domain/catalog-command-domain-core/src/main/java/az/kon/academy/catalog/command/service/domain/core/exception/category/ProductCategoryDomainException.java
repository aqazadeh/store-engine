package az.kon.academy.catalog.command.service.domain.core.exception.category;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductCategoryDomainException extends CatalogDomainException {
    public ProductCategoryDomainException(String code) {
        super("category", code);
    }

    public ProductCategoryDomainException(String code, Throwable cause) {
        super("category", code, cause);
    }

    public ProductCategoryDomainException(String code, List<String> args) {
        super("category", code, args);
    }

    public ProductCategoryDomainException(String code, List<String> args, Throwable cause) {
        super("category", code, args, cause);
    }
}
