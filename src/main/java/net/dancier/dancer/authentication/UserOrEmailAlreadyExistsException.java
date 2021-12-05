package net.dancier.dancer.authentication;

import net.dancier.dancer.core.exception.AppliationException;

public class UserOrEmailAlreadyExistsException extends AppliationException {

    public UserOrEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserOrEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
