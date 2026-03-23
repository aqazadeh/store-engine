package az.kon.academy.exception;

import java.util.List;

public class SeDomainException extends SeException {
    private final String module;
    private final String domain;
    private final String code;
    private final List<String> args;

    public SeDomainException(String module, String domain, String code) {
        this(module, domain, code, List.of(), null);
    }

    public SeDomainException(String module, String domain, String code, Throwable cause) {
        this(module, domain, code, List.of(), cause);
    }

    public SeDomainException(String module, String domain, String code, List<String> args) {
        this(module, domain, code, args, null);
    }

    public SeDomainException(String module, String domain, String code, List<String> args, Throwable cause) {
        super(String.format("%s.%s.%s", module, domain, code), cause);
        this.module = module;
        this.domain = domain;
        this.code = code;
        this.args = args;
    }

    public String getModule() {
        return module;
    }

    public String getDomain() {
        return domain;
    }

    public String getCode() {
        return code;
    }

    public List<String> getArgs() {
        return args;
    }
}
