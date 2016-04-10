package com.cnki.exception;

/**
 * Created by yjh on 2016/3/21.
 */
public class ConcurrentException extends Exception {
    public ConcurrentException() {
    }

    public ConcurrentException(String message) {
        super(message);
    }

    public ConcurrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcurrentException(Throwable cause) {
        super(cause);
    }

    public ConcurrentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
