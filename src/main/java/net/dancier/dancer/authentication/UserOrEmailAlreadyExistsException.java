package net.dancier.dancer.authentication;

import net.dancier.dancer.core.exception.AppException;

public class UserOrEmailAlreadyExistsException extends AppException {

    public UserOrEmailAlreadyExistsException(String message) {
        super(message);
    }

    public UserOrEmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
