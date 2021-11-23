package net.dancier.dancer.authentication;

public class UserOrEmailAlreadyExistsException extends RuntimeException {
    public UserOrEmailAlreadyExistsException(String message) {
        super(message);
    }
}
