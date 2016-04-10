package com.cnki.exception;

/**
 * Created by yjh on 2016/3/21.
 */
public class DownloadException extends Exception {
    public DownloadException() {
    }

    public DownloadException(String message) {
        super(message);
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }

    public DownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
