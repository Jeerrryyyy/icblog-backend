package io.ic1101.icblog.api.utils.exception.mapper;

import io.ic1101.icblog.api.utils.dto.ErrorDto;
import io.ic1101.icblog.api.utils.exception.custom.AuthHeaderMissingException;
import io.ic1101.icblog.api.utils.exception.custom.JwtParsingException;
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

    @ExceptionHandler(value = {JwtParsingException.class})
    public ResponseEntity<?> handleException(JwtParsingException jwtParsingException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(jwtParsingException.getMessage()));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto(exception.getMessage()));
    }
}
