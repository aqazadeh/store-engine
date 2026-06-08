package az.kon.academy.catalog.command.service.domain.core.exception.product;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductPriceDomainException extends CatalogDomainException {

    public ProductPriceDomainException(String code) {
        super("product-price", code);
    }

    public ProductPriceDomainException(String code, Throwable cause) {
        super("product-price", code, cause);
    }

    public ProductPriceDomainException(String code, List<String> args) {
        super("product-price", code, args);
    }

    public ProductPriceDomainException(String code, List<String> args, Throwable cause) {
        super("product-price", code, args, cause);
    }
}
