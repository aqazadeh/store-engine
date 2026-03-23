package az.kon.academy.exception;

import java.util.List;

public class SeEntityNotFoundException extends SeDaoException {

    public SeEntityNotFoundException(String module, String domain, String code) {
        super(module, domain, code);
    }

    public SeEntityNotFoundException(String module, String domain, String code, Throwable cause) {
        super(module, domain, code, cause);
    }

    public SeEntityNotFoundException(String module, String domain, String code, List<String> args) {
        super(module, domain, code, args);
    }

    public SeEntityNotFoundException(String module, String domain, String code, List<String> args, Throwable cause) {
        super(module, domain, code, args, cause);
    }
}
