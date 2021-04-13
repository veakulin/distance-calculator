package ru.akulin.exception;

public class CityControllerException extends RuntimeException {
    public CityControllerException() {
        super();
    }

    public CityControllerException(String message) {
        super(message);
    }

    public CityControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CityControllerException(Throwable cause) {
        super(cause);
    }

    protected CityControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
