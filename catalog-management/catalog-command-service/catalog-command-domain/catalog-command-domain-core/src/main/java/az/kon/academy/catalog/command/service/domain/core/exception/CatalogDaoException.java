package az.kon.academy.catalog.command.service.domain.core.exception;

import az.kon.academy.exception.SeDaoException;

import java.util.List;

public class CatalogDaoException extends SeDaoException {
    public CatalogDaoException(String domain, String code) {
        super("catalog", domain, code);
    }

    public CatalogDaoException(String domain, String code, Throwable cause) {
        super("catalog", domain, code, cause);
    }

    public CatalogDaoException(String domain, String code, List<String> args) {
        super("catalog", domain, code, args);
    }

    public CatalogDaoException(String domain, String code, List<String> args, Throwable cause) {
        super("catalog", domain, code, args, cause);
    }
}
