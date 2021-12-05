package net.dancier.dancer.core.exception;

public class NotFoundException extends AppliationException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
