package io.ic1101.icblog.api.utils.exception.custom;

public class AuthHeaderMissingException extends RuntimeException {

    public AuthHeaderMissingException(String message) {
        super(message);
    }
}
