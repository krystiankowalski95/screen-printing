package pl.lodz.it.sitodruk.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lodz.it.sitodruk.exceptions.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = { BaseException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "unexpected.error";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}