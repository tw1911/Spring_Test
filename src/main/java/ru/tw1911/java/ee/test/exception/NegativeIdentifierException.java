package ru.tw1911.java.ee.test.exception;

public class NegativeIdentifierException extends Exception{
    public NegativeIdentifierException() {
        super("id не может быть меньше 0");
    }

    public NegativeIdentifierException(String message) {
        super(message);
    }

    public NegativeIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public NegativeIdentifierException(Throwable cause) {
        super(cause);
    }

    public NegativeIdentifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
