package io.ic1101.icblog.api.user.exception.mapper;

import io.ic1101.icblog.api.user.exception.custom.EmailAlreadyExistsException;
import io.ic1101.icblog.api.user.exception.custom.UserOrRoleNotFoundException;
import io.ic1101.icblog.api.utils.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {EmailAlreadyExistsException.class})
    public ResponseEntity<?> handleException(EmailAlreadyExistsException emailAlreadyExistsException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(emailAlreadyExistsException.getMessage()));
    }

    @ExceptionHandler(value = {UserOrRoleNotFoundException.class})
    public ResponseEntity<?> handleException(UserOrRoleNotFoundException userOrRoleNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(userOrRoleNotFoundException.getMessage()));
    }
}
