package az.kon.academy.exception;

public class SeException extends RuntimeException {

    public SeException(String message) {
        super(message);
    }

    public SeException(String message, Throwable cause) {
        super(message, cause);
    }
}
