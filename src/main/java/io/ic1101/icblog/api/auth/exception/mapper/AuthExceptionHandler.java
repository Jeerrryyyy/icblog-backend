package io.ic1101.icblog.api.auth.exception.mapper;

import io.ic1101.icblog.api.auth.exception.custom.TokenAlreadyInvalidatedException;
import io.ic1101.icblog.api.utils.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(value = {TokenAlreadyInvalidatedException.class})
    public ResponseEntity<?> handleException(TokenAlreadyInvalidatedException tokenAlreadyInvalidatedException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(tokenAlreadyInvalidatedException.getMessage()));
    }
}
