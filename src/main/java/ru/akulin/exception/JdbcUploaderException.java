package ru.akulin.exception;

public class JdbcUploaderException extends RuntimeException {

    public JdbcUploaderException() {
        super();
    }

    public JdbcUploaderException(String message) {
        super(message);
    }

    public JdbcUploaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcUploaderException(Throwable cause) {
        super(cause);
    }

    protected JdbcUploaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
