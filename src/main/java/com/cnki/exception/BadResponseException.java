package com.cnki.exception;

/**
 * Created by yjh on 2016/3/21.
 */
public class BadResponseException extends Exception {
    public BadResponseException() {
    }

    public BadResponseException(String message) {
        super(message);
    }

    public BadResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadResponseException(Throwable cause) {
        super(cause);
    }

    public BadResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
