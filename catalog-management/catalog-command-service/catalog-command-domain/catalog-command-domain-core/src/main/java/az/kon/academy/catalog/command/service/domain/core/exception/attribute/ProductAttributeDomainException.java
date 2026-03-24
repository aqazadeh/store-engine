package az.kon.academy.catalog.command.service.domain.core.exception.attribute;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductAttributeDomainException extends CatalogDomainException {
    public ProductAttributeDomainException(String code) {
        super("attribute", code);
    }

    public ProductAttributeDomainException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public ProductAttributeDomainException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public ProductAttributeDomainException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
