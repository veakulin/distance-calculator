package ru.akulin.exception;

public class InputFileParserException extends RuntimeException {

    public InputFileParserException() {
        super();
    }

    public InputFileParserException(String message) {
        super(message);
    }

    public InputFileParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputFileParserException(Throwable cause) {
        super(cause);
    }

    protected InputFileParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
