package az.kon.academy.catalog.command.service.domain.core.exception.specification;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class SpecificationDomainException extends CatalogDomainException {
    public SpecificationDomainException(String code) {
        super("attribute", code);
    }

    public SpecificationDomainException(String code, Throwable cause) {
        super("attribute", code, cause);
    }

    public SpecificationDomainException(String code, List<String> args) {
        super("attribute", code, args);
    }

    public SpecificationDomainException(String code, List<String> args, Throwable cause) {
        super("attribute", code, args, cause);
    }
}
