package ru.akulin.exception;

public class DistanceEvaluatorException extends RuntimeException {

    public DistanceEvaluatorException() {
        super();
    }

    public DistanceEvaluatorException(String message) {
        super(message);
    }

    public DistanceEvaluatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistanceEvaluatorException(Throwable cause) {
        super(cause);
    }

    protected DistanceEvaluatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
