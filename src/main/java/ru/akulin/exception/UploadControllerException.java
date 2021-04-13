package ru.akulin.exception;

public class UploadControllerException extends RuntimeException {

    public UploadControllerException() {
        super();
    }

    public UploadControllerException(String message) {
        super(message);
    }

    public UploadControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadControllerException(Throwable cause) {
        super(cause);
    }

    protected UploadControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
