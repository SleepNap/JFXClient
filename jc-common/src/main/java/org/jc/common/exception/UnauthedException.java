package org.jc.common.exception;

public class UnauthedException extends RuntimeException {

    public UnauthedException() {
        super();
    }

    public UnauthedException(String message) {
        super(message);
    }

    public UnauthedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthedException(Throwable cause) {
        super(cause);
    }

    protected UnauthedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
