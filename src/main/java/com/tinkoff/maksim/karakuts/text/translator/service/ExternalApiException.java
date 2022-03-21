package com.tinkoff.maksim.karakuts.text.translator.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
    reason = "Error occurred while reaching external translation API")
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
