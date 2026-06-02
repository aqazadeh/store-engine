package az.kon.academy.catalog.command.service.domain.core.exception.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductSpecificationDomainException extends CatalogDomainException {
    public ProductSpecificationDomainException(String code) {
        super("attribute", code);
    }

    public ProductSpecificationDomainException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public ProductSpecificationDomainException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public ProductSpecificationDomainException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
