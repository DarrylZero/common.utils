package com.steammachine.common.validation;

/**
 *
 * Ошибка проверки значений.
 *
 * Created 31.12.2017 18:10
 * @author Vladimir Bogodukhov
 **/
public class ValidationFailed extends RuntimeException {

    public ValidationFailed() {
    }

    public ValidationFailed(String message) {
        super(message);
    }

    public ValidationFailed(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationFailed(Throwable cause) {
        super(cause);
    }

    public ValidationFailed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
