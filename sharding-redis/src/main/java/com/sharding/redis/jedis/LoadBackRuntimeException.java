package com.sharding.redis.jedis;

/**
 * 回源异常。
 *
 */
public class LoadBackRuntimeException extends RuntimeException {

    public LoadBackRuntimeException() {
    }

    public LoadBackRuntimeException(String message) {
        super(message);
    }

    public LoadBackRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadBackRuntimeException(Throwable cause) {
        super(cause);
    }

}
