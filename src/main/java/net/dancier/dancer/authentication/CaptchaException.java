package net.dancier.dancer.authentication;

import net.dancier.dancer.core.exception.ApplicationException;

public class CaptchaException extends ApplicationException {

    public CaptchaException(String message) {
        super(message);
    }

}
