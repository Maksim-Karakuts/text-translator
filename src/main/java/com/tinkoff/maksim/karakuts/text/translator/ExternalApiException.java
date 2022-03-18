package com.tinkoff.maksim.karakuts.text.translator;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException() {
    }

    public ExternalApiException(String message) {
        super(message);
    }

    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
