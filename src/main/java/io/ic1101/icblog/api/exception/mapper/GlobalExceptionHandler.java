package io.ic1101.icblog.api.exception.mapper;

import io.ic1101.icblog.api.dto.response.ErrorDto;
import io.ic1101.icblog.api.exception.custom.AuthHeaderMissingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AuthHeaderMissingException.class})
    public ResponseEntity<?> handleException(AuthHeaderMissingException authHeaderMissingException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(authHeaderMissingException.getMessage()));
    }
}
