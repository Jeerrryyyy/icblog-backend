package io.ic1101.icblog.api.utils.exception.custom;

public class JwtParsingException extends RuntimeException {

    public JwtParsingException(String message) {
        super(message);
    }
}
