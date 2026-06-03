package az.kon.academy.catalog.command.service.adapter.inbound.rest.temp.exception.advice;

import az.kon.academy.catalog.command.service.adapter.inbound.rest.ErrorResponse;
import az.kon.academy.exception.SeDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralControllerAdvice {
    private final MessageSource messageSource;

    public GeneralControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(SeDomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(SeDomainException exception) {
//        log.error(exception.getMessage(), exception);
        var message = messageSource.getMessage(exception.getMessage(), exception.getArgs().toArray(), null);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                     .body(new ErrorResponse(exception.getCode(), message));}


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("000000", "UNEXPECTED ERROR"));
    }
}
