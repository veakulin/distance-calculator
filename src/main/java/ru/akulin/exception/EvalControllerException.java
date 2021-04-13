package ru.akulin.exception;

public class EvalControllerException extends RuntimeException {
    public EvalControllerException() {
        super();
    }

    public EvalControllerException(String message) {
        super(message);
    }

    public EvalControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalControllerException(Throwable cause) {
        super(cause);
    }

    protected EvalControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
