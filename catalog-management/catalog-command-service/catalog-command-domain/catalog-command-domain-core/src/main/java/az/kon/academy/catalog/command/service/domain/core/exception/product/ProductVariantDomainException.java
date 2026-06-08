package az.kon.academy.catalog.command.service.domain.core.exception.product;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductVariantDomainException extends CatalogDomainException {

    public ProductVariantDomainException(String code) {
        super("product-variant", code);
    }

    public ProductVariantDomainException(String code, Throwable cause) {
        super("product-variant", code, cause);
    }

    public ProductVariantDomainException(String code, List<String> args) {
        super("product-variant", code, args);
    }

    public ProductVariantDomainException(String code, List<String> args, Throwable cause) {
        super("product-variant", code, args, cause);
    }
}
