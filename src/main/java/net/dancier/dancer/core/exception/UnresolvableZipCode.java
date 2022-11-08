package net.dancier.dancer.core.exception;

public class UnresolvableZipCode extends BusinessException {

    public UnresolvableZipCode(String message) {
        super(message);
    }

    public UnresolvableZipCode(String message, Throwable throwable) {
        super(message, throwable);
    }
}
