
package com.sharding.redis.jedis;

/**
 * RedisClient运行时异常。
 */
public class RedisClientRuntimeException extends RuntimeException{

	public RedisClientRuntimeException() {
		super();
	}

	public RedisClientRuntimeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RedisClientRuntimeException(String arg0) {
		super(arg0);
	}

	public RedisClientRuntimeException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = 1L;

}
