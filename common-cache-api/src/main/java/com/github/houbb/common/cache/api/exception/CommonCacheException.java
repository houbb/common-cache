package com.github.houbb.common.cache.api.exception;

/**
 * 通用缓存异常
 * @since 0.0.4
 * @author dh
 */
public class CommonCacheException extends RuntimeException {

    public CommonCacheException() {
    }

    public CommonCacheException(String message) {
        super(message);
    }

    public CommonCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonCacheException(Throwable cause) {
        super(cause);
    }

    public CommonCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
