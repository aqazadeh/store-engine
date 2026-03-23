package az.kon.academy.exception;

import java.util.List;

public class SeDaoException extends SeDomainException{

    public SeDaoException(String module, String domain, String code) {
        super(module, domain, code);
    }

    public SeDaoException(String module, String domain, String code, Throwable cause) {
        super(module, domain, code, cause);
    }

    public SeDaoException(String module, String domain, String code, List<String> args) {
        super(module, domain, code, args);
    }

    public SeDaoException(String module, String domain, String code, List<String> args, Throwable cause) {
        super(module, domain, code, args, cause);
    }
}
