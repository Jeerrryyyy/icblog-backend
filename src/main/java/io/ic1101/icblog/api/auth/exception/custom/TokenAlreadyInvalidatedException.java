package io.ic1101.icblog.api.auth.exception.custom;

public class TokenAlreadyInvalidatedException extends RuntimeException {

    public TokenAlreadyInvalidatedException(String message) {
        super(message);
    }
}
