package net.dancier.dancer.authentication;

import net.dancier.dancer.core.exception.AppliationException;

public class CaptchaException extends AppliationException {

    public CaptchaException(String message) {
        super(message);
    }

}
