package az.kon.academy.catalog.command.service.domain.core.exception.product;

import az.kon.academy.catalog.command.service.domain.core.exception.CatalogDomainException;

import java.util.List;

public class ProductStockDomainException extends CatalogDomainException {

    public ProductStockDomainException(String code) {
        super("product-stock", code);
    }

    public ProductStockDomainException(String code, Throwable cause) {
        super("product-stock", code, cause);
    }

    public ProductStockDomainException(String code, List<String> args) {
        super("product-stock", code, args);
    }

    public ProductStockDomainException(String code, List<String> args, Throwable cause) {
        super("product-stock", code, args, cause);
    }
}