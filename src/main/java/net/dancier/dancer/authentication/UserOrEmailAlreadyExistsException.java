package net.dancier.dancer.authentication;

import net.dancier.dancer.core.exception.ApplicationException;

public class UserOrEmailAlreadyExistsException extends ApplicationException {

    public UserOrEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserOrEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
