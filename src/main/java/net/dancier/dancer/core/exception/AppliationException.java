package net.dancier.dancer.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppliationException extends RuntimeException{
    public AppliationException(String message) {
        super(message);
    }

    public AppliationException(String message, Throwable cause) {
        super(message, cause);
    }
}
