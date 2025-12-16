package com.example.order.common.cache;

import com.example.order.common.exception.BusinessException;

/**
 * 缓存异常类
 */
public class CacheException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public CacheException(int code, String message) {
        super(code, message);
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
