
package com.sharding.redis.jedis.support;

//import static com.le.jr.platform.redis.RedisContants.CS_UTF8;
//import static com.le.jr.platform.redis.RedisContants.REDIS_REPLY_STATUS_OK;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import com.sharding.redis.jedis.*;
import com.sharding.redis.jedis.statistic.RedisClusterStatistic;
import com.sharding.redis.jedis.statistic.RedisGroupStatistic;
import com.sharding.redis.jedis.statistic.RedisStatistic;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import static com.sharding.redis.jedis.RedisContants.CS_UTF8;
import static com.sharding.redis.jedis.RedisContants.REDIS_REPLY_STATUS_OK;

/**
 * 基于Jedis实现的支持分片、读写分离的RedisClient。
 */
public class ShardedRedisClient implements RedisClient {

	/**
	 * 方法执行模板。
	 */
	private ShardedJedisTemplate template;

	/**
	 * 回源客户端。
	 */
	private LoadingBackClient loadingBackClient;
	
	public ShardedRedisClient(ShardedJedisTemplate template) {
		super();
		this.template = template;
		this.loadingBackClient = new LoadingBackClientImpl(this);
	}
	
	public ShardedRedisClient() {
		super();
		this.loadingBackClient = new LoadingBackClientImpl(this);
	}
	
	public void setTemplate(ShardedJedisTemplate template) {
		this.template = template;
	}

	private static final Gson GSON =
			new GsonBuilder()
					.setDateFormat("yyyy-MM-dd HH:mm:ss")//设置时间格式
					//指定map实现类型
					.registerTypeAdapter(Map.class, new JsonDeserializer<Map<String,Object>>() {
						@Override
						public Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
							JsonObject jsonObject = json.getAsJsonObject();
							Set<Map.Entry<String, JsonElement>> set = jsonObject.entrySet();
							Map<String, Object> map = new HashMap<>(set.size(), 1.0f);
							for(Map.Entry<String, JsonElement> entry : set){
								map.put(entry.getKey(), entry.getValue().getAsString());
							}
							return map;
						}
					})
					.create();

	@Override
	public LoadingBackClient getLoadingBackClient() {
		return loadingBackClient;
	}

	private static class LoadingBackClientImpl implements LoadingBackClient{

		private RedisClient redisClient;

		public LoadingBackClientImpl(RedisClient redisClient){
			this.redisClient = redisClient;
		}

		@Override
		public String get(String key, BackLoader<String> backLoader, int expireSecond) {
			String value;
			try{
				value = redisClient.getString(key);
				if(value == null || value.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return value;
			} catch (Throwable e){
				try{
					value = backLoader.load(key, e);
					if(value != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, value, expireSecond);
						} catch (Throwable ignore){
						}
					}
					return value;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
		}

        @Override
        public Long getLong(String key, BackLoader<Long> backLoader, int expireSecond) {
			String value;
			try{
				value = redisClient.getString(key);
				if(value == null || value.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return Long.parseLong(value);
			} catch (Throwable e){
				try{
					Long longValue = backLoader.load(key, e);
					if(longValue != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, longValue.toString(), expireSecond);
						} catch (Throwable ignore){
						}
					}
					return longValue;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
        }

        @Override
        public Integer getInt(String key, BackLoader<Integer> backLoader, int expireSecond) {
			String value;
			try{
				value = redisClient.getString(key);
				if(value == null || value.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return Integer.parseInt(value);
			} catch (Throwable e){
				try{
					Integer intValue = backLoader.load(key, e);
					if(intValue != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, intValue.toString(), expireSecond);
						} catch (Throwable ignore){
						}
					}
					return intValue;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
        }

        @Override
        public Double getDouble(String key, BackLoader<Double> backLoader, int expireSecond) {
			String value;
			try{
				value = redisClient.getString(key);
				if(value == null || value.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return Double.parseDouble(value);
			} catch (Throwable e){
				try{
					Double doubleValue = backLoader.load(key, e);
					if(doubleValue != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, doubleValue.toString(), expireSecond);
						} catch (Throwable ignore){
						}
					}
					return doubleValue;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
        }

		@Override
		public <O> O getFromJson(String key, BackLoader<O> backLoader, Class<O> objectClass, int expireSecond) {
			String jsonValue;
			try{
				jsonValue = redisClient.getString(key);
				if(jsonValue == null || jsonValue.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return GSON.fromJson(jsonValue, objectClass);
			} catch (Throwable e){
				try{
					O value = backLoader.load(key, e);
					if(value != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, GSON.toJson(value), expireSecond);
						} catch (Throwable ignore){
						}
					}
					return value;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
		}

		@Override
		public <T> T getFromJson(String key, BackLoader<T> backLoader, Type typeOfT, int expireSecond) {
			String jsonValue;
			try{
				jsonValue = redisClient.getString(key);
				if(jsonValue == null || jsonValue.trim().length() == 0){
					throw new CacheMissException(key);
				}
				return GSON.fromJson(jsonValue, typeOfT);
			} catch (Throwable e){
				try{
					T value = backLoader.load(key, e);
					if(value != null){
						//回源后设置缓存不要影响回源逻辑
						try{
							redisClient.set(key, GSON.toJson(value), expireSecond);
						} catch (Throwable ignore){
						}
					}
					return value;
				} catch (Throwable e1){
					//回源异常。
					throw new LoadBackRuntimeException(e1);
				}
			}
		}

	}

	@Override
	public void set(final String key, final String value) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				String reply = jedis.set(key, value);
				if(!REDIS_REPLY_STATUS_OK.equals(reply)){
					throw new RedisClientRuntimeException("the reply of set is ["+reply+"]");
				}
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public String getString(final String key) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.get(key);
			}
		}, RW.READ);
	}

	@Override
	public void set(final String key, final byte[] value) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				String reply = jedis.set(key.getBytes(CS_UTF8), value);
				if(!REDIS_REPLY_STATUS_OK.equals(reply)){
					throw new RedisClientRuntimeException("the reply of set is ["+reply+"]");
				}
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public byte[] get(final String key) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.get(key.getBytes(CS_UTF8));
			}
		}, RW.READ);
	}
	
	@Override
	public long delete(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.del(key);
			}
		}, RW.WRITE);
	}

	@Override
	public boolean set(final String key, final String value, final NX nx) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(nx == null){
					throw new IllegalArgumentException("option:[NX] of set can't be null!");
				}
				String reply = jedis.set(key, value, nx.getCode());
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean set(final String key, final String value, final NX nx, final TimeUnit timeUnit, final long expires) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(nx == null){
					throw new IllegalArgumentException("option:[NX] of set can't be null!");
				}
				if(timeUnit == null){
					throw new IllegalArgumentException("option:[TimeUnit] of set can't be null!");
				}
				String reply = jedis.set(key, value, nx.getCode(), timeUnit.getUnit(), expires);
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean set(final String key, final byte[] value, final NX nx) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(nx == null){
					throw new IllegalArgumentException("option:[NX] of set can't be null!");
				}
				String reply = jedis.set(key.getBytes(CS_UTF8), value, nx.getCode().getBytes(CS_UTF8));
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean set(final String key, final byte[] value, final NX nx, final TimeUnit timeUnit, final long expires) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(nx == null){
					throw new IllegalArgumentException("option:[NX] of set can't be null!");
				}
				if(timeUnit == null){
					throw new IllegalArgumentException("option:[TimeUnit] of set can't be null!");
				}
				String reply = jedis.set(key.getBytes(CS_UTF8), value, nx.getCode().getBytes(CS_UTF8), timeUnit.getUnit().getBytes(CS_UTF8), expires);
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean exists(final String key) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.exists(key);
			}
		}, RW.READ);
	}

	@Override
	public long incr(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.incr(key);
			}
		}, RW.WRITE);
	}

	@Override
	public long incrBy(final String key, final long increment) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.incrBy(key, increment);
			}
		}, RW.WRITE);
	}

	@Override
	public double incrByFloat(final String key, final double increment) {
		return template.execute(key, new JedisCallback<Double>() {
			@Override
			public Double call(Jedis jedis) {
				return jedis.incrByFloat(key, increment);
			}
		}, RW.WRITE);
	}

	@Override
	public long decr(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.decr(key);
			}
		}, RW.WRITE);
	}

	@Override
	public long decrBy(final String key, final long decrement) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.decrBy(key, decrement);
			}
		}, RW.WRITE);
	}

	@Override
	public long append(final String key, final String value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(value == null){
					throw new IllegalArgumentException("append value can't be null!");
				}
				return jedis.append(key, value);
			}
		}, RW.WRITE);
	}

	@Override
	public long hset(final String key, final String field, final String value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		}, RW.WRITE);
	}

	@Override
	public long hset(final String key, final String field, final byte[] value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hset(key.getBytes(CS_UTF8), field.getBytes(CS_UTF8), value);
			}
		}, RW.WRITE);
	}

	@Override
	public long hsetnx(final String key, final String field, final String value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hsetnx(key, field, value);
			}
		}, RW.WRITE);
	}

	@Override
	public long hsetnx(final String key, final String field, final byte[] value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hsetnx(key.getBytes(CS_UTF8), field.getBytes(CS_UTF8), value);
			}
		}, RW.WRITE);
	}

	@Override
	public String hgetString(final String key, final String field) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.hget(key, field);
			}
		}, RW.READ);
	}

	@Override
	public byte[] hget(final String key, final String field) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.hget(key.getBytes(CS_UTF8), field.getBytes(CS_UTF8));
			}
		}, RW.READ);
	}

	@Override
	public Map<String, String> hgetAllString(final String key) {
		return template.execute(key, new JedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> call(Jedis jedis) {
				return jedis.hgetAll(key);
			}
		}, RW.READ);
	}

	@Override
	public long hdel(final String key, final String... fields) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(fields == null || fields.length == 0){
					throw new IllegalArgumentException("the fields to del can't be empty!");
				}
				return jedis.hdel(key, fields);
			}
		}, RW.WRITE);
	}

	@Override
	public void hmsetString(final String key, final Map<String, String> fieldValues) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				if(fieldValues == null || fieldValues.isEmpty()){
					throw new IllegalArgumentException("fieldvalues of hmset can't be null!");
				}
				String reply = jedis.hmset(key, fieldValues);
				if(!REDIS_REPLY_STATUS_OK.equals(reply)){
					throw new RedisClientRuntimeException("the reply of hmset is ["+reply+"]");
				}
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public void hmset(final String key, final Map<String, byte[]> fieldValues) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				if(fieldValues == null || fieldValues.isEmpty()){
					throw new IllegalArgumentException("fieldvalues of hmset can't be null!");
				}
				HashMap<byte[], byte[]> hash = new HashMap<byte[], byte[]>(fieldValues.size(), 1.0f);
				for(String field : fieldValues.keySet()){
					hash.put(field.getBytes(CS_UTF8), fieldValues.get(field));
				}
				String reply = jedis.hmset(key.getBytes(CS_UTF8), hash);
				if(!REDIS_REPLY_STATUS_OK.equals(reply)){
					throw new RedisClientRuntimeException("the reply of hmset is ["+reply+"]");
				}
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public boolean hexists(final String key, final String field) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		}, RW.READ);
	}

	@Override
	public Set<String> hkeys(final String key) {
		return template.execute(key, new JedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis) {
				return jedis.hkeys(key);
			}
		}, RW.READ);
	}

	@Override
	public long hlen(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hlen(key);
			}
		}, RW.READ);
	}

	@Override
	public List<String> hStringVals(final String key) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				return jedis.hvals(key);
			}
		}, RW.READ);
	}

	@Override
	public long hincrBy(final String key, final String field, final long increment) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.hincrBy(key, field, increment);
			}
		}, RW.WRITE);
	}

	@Override
	public List<String> hmgetString(final String key, final String... fields) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				if(fields == null || fields.length == 0){
					throw new IllegalArgumentException("the fields to hmgetString can't be empty!");
				}
				return jedis.hmget(key, fields);
			}
		}, RW.READ);
	}

	@Override
	public List<byte[]> hmget(final String key, final String... fields) {
		return template.execute(key, new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> call(Jedis jedis) {
				if(fields == null || fields.length == 0){
					throw new IllegalArgumentException("the fields to hmget can't be empty!");
				}
				int length = fields.length;
				byte[] fs[] = new byte[length][];
				for(int i=0;i<length;i++){
					fs[i] = fields[i].getBytes(CS_UTF8);
				}
				return jedis.hmget(key.getBytes(CS_UTF8), fs);
			}
		}, RW.READ);
	}

	@Override
	public boolean setnx(final String key, final String value) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.setnx(key, value).intValue() == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public boolean setnx(final String key, final byte[] value) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.setnx(key.getBytes(CS_UTF8), value).intValue() == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public String getSet(final String key, final String value) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.getSet(key, value);
			}
		}, RW.WRITE);
	}

	@Override
	public byte[] getSet(final String key, final byte[] value) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.getSet(key.getBytes(CS_UTF8), value);
			}
		}, RW.WRITE);
	}

	@Override
	public String lindexString(final String key, final long index) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.lindex(key, index);
			}
		}, RW.READ);
	}

	@Override
	public byte[] lindex(final String key, final long index) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.lindex(key.getBytes(CS_UTF8), index);
			}
		}, RW.READ);
	}

	@Override
	public long lpush(final String key, final String... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of lpush can't be empty!");
				}
				return jedis.lpush(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public long lpush(final String key, final byte[]... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of lpush can't be empty!");
				}
				return jedis.lpush(key.getBytes(CS_UTF8), values);
			}
		}, RW.WRITE);
	}

	@Override
	public String lpopString(final String key) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.lpop(key);
			}
		}, RW.READ);
	}

	@Override
	public byte[] lpop(final String key) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.lpop(key.getBytes(CS_UTF8));
			}
		}, RW.READ);
	}

	@Override
	public long lpushx(final String key, final String ... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of lpushx can't be empty!");
				}
				return jedis.lpushx(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public long lpushx(final String key, final byte[]... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of lpushx can't be empty!");
				}
				return jedis.lpushx(key.getBytes(CS_UTF8), values);
			}
		}, RW.WRITE);
	}

	@Override
	public void lset(final String key, final long index, final String value) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				jedis.lset(key, index, value);
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public void lset(final String key, final long index, final byte[] value) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				jedis.lset(key.getBytes(CS_UTF8), index, value);
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public long llen(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.llen(key);
			}
		}, RW.READ);
	}

	@Override
	public long lrem(final String key, final long count, final String value) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.lrem(key, count, value);
			}
		}, RW.WRITE);
	}

	@Override
	public List<String> lrangeString(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				return jedis.lrange(key, start, end);
			}
		}, RW.READ);
	}

	@Override
	public List<byte[]> lrange(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> call(Jedis jedis) {
				return jedis.lrange(key.getBytes(CS_UTF8), start, end);
			}
		}, RW.READ);
	}

	@Override
	public long rpush(final String key, final String... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of rpush can't be empty!");
				}
				return jedis.rpush(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public long rpush(final String key, final byte[]... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of rpush can't be empty!");
				}
				return jedis.lpush(key.getBytes(CS_UTF8), values);
			}
		}, RW.WRITE);
	}

	@Override
	public String rpopString(final String key) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				return jedis.rpop(key);
			}
		}, RW.READ);
	}

	@Override
	public byte[] rpop(final String key) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				return jedis.rpop(key.getBytes(CS_UTF8));
			}
		}, RW.READ);
	}

	@Override
	public long rpushx(final String key, final String... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of rpushx can't be empty!");
				}
				return jedis.rpushx(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public long rpushx(final String key, final byte[]... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of rpushx can't be empty!");
				}
				return jedis.rpushx(key.getBytes(CS_UTF8), values);
			}
		}, RW.WRITE);
	}

	@Override
	public boolean sadd(final String key, final String ... values) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of sadd can't be empty!");
				}
				return jedis.sadd(key, values) == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public boolean sadd(final String key, final byte[] ... values) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of sadd can't be empty!");
				}
				return jedis.sadd(key.getBytes(CS_UTF8), values) == 1;
			}
		}, RW.WRITE);
	}
	
	@Override
	public Set<byte[]> bsmembers(final String key) {
		return template.execute(key, new JedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> call(Jedis jedis) {
				byte[]oneKey = key.getBytes(CS_UTF8);
				return jedis.smembers(oneKey);
			}
		}, RW.READ);
	}
	
	@Override
	public Set<String> smembers(final String key) {
		return template.execute(key, new JedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis) {
				Set<String> result = jedis.smembers(key);
				return result;
			}
		}, RW.READ);
	}
	
	@Override
	public boolean expire(final String key, final int seconds) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.expire(key, seconds) == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public boolean setex(final String key, final String value, final int expires) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				String reply = jedis.setex(key, expires, value);
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean setex(final String key, final byte[] value, final int expires) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				String reply = jedis.setex(key.getBytes(CS_UTF8), expires, value);
				if(REDIS_REPLY_STATUS_OK.equals(reply)){
					return true;
				}else{
					return false;
				}
			}
		}, RW.WRITE);
	}

	@Override
	public boolean zadd(final String key, final String value, final double score) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.zadd(key, score, value) == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public boolean zadd(final String key, final byte[] value, final double score) {
		return template.execute(key, new JedisCallback<Boolean>() {
			@Override
			public Boolean call(Jedis jedis) {
				return jedis.zadd(key.getBytes(CS_UTF8), score, value) == 1;
			}
		}, RW.WRITE);
	}

	@Override
	public long zaddString(final String key, final Map<String, Double> valueScorePairs) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(valueScorePairs == null || valueScorePairs.size() == 0){
					throw new IllegalArgumentException("valueScorePairs of zaddString can't be empty!");
				}
				return jedis.zadd(key, valueScorePairs);
			}
		}, RW.WRITE);
	}

	@Override
	public long zadd(final String key, final Map<byte[], Double> valueScorePairs) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(valueScorePairs == null || valueScorePairs.size() == 0){
					throw new IllegalArgumentException("valueScorePairs of zaddString can't be empty!");
				}
				return jedis.zadd(key.getBytes(CS_UTF8), valueScorePairs);
			}
		}, RW.WRITE);
	}

	@Override
	public long zrem(final String key, final String... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of zrem can't be empty!");
				}
				return jedis.zrem(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public long zrem(final String key, final byte[]... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of zrem can't be empty!");
				}
				return jedis.zrem(key.getBytes(CS_UTF8), values);
			}
		}, RW.WRITE);
	}

	@Override
	public long zcard(final String key) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				return jedis.zcard(key);
			}
		}, RW.READ);
	}

	@Override
	public Set<String> zrangeString(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis) {
				return jedis.zrange(key, start, end);
			}
		}, RW.READ);
	}

	@Override
	public Set<byte[]> zrange(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> call(Jedis jedis) {
				return jedis.zrange(key.getBytes(CS_UTF8), start, end);
			}
		}, RW.READ);
	}

	@Override
	public Set<Tuple> zrangeStringWithScores(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> call(Jedis jedis) {
				return jedis.zrangeWithScores(key, start, end);
			}
		}, RW.READ);
	}

	@Override
	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> call(Jedis jedis) {
				return jedis.zrangeWithScores(key, start, end);
			}
		}, RW.READ);
	}
	
	@Override
	public double zincrby(final String key, final String value, final double score) {
		return template.execute(key, new JedisCallback<Double>() {
			@Override
			public Double call(Jedis jedis) {
				return jedis.zincrby(key, score, value);
			}
		}, RW.WRITE);
	}
	
	@Override
	public Set<byte[]> zrevrange(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> call(Jedis jedis) {
				return jedis.zrevrange(key.getBytes(CS_UTF8), start, end);
			}
		}, RW.READ);
	}

	@Override
	public Set<String> zrevrangeString(final String key, final long start, final long end) {
		return template.execute(key, new JedisCallback<Set<String>>() {
			@Override
			public Set<String> call(Jedis jedis) {
				return jedis.zrevrange(key, start, end);
			}
		}, RW.READ);
	}
	
	@Override
	public Double zscore(final String key, final String value) {
		return template.execute(key, new JedisCallback<Double>() {
			@Override
			public Double call(Jedis jedis) {
				return jedis.zscore(key, value);
			}
		}, RW.READ);
	}

	@Override
	public void set(final String key, final String value, final int expireSecond) {
		template.execute(key, new JedisCallback<Void>() {
			@Override
			public Void call(Jedis jedis) {
				String reply = jedis.set(key, value);
				if(!REDIS_REPLY_STATUS_OK.equals(reply)){
					throw new RedisClientRuntimeException("the reply of set is ["+reply+"]");
				}
				//设置超时时间。
				jedis.expire(key, expireSecond);
				return null;
			}
		}, RW.WRITE);
	}

	@Override
	public String blpopString(final String key) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				List<String> result = jedis.blpop(Integer.MAX_VALUE, key);
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public byte[] blpop(final String key) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				byte[][] oneKey = new byte[][]{key.getBytes(CS_UTF8)};
				List<byte[]> result = jedis.blpop(Integer.MAX_VALUE, oneKey);
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public String brpopString(final String key) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				List<String> result = jedis.brpop(Integer.MAX_VALUE, key);
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public byte[] brpop(final String key) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				byte[][] oneKey = new byte[][]{key.getBytes(CS_UTF8)};
				List<byte[]> result = jedis.brpop(Integer.MAX_VALUE, oneKey);
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public String blpopString(final String key, final int timeout) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				List<String> result = jedis.blpop(timeout, key);
				if(result == null){
					//超时。
					return null;
				}
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public byte[] blpop(final String key, final int timeout) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				byte[][] oneKey = new byte[][]{key.getBytes(CS_UTF8)};
				List<byte[]> result = jedis.blpop(timeout, oneKey);
				if(result == null){
					//超时。
					return null;
				}
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public String brpopString(final String key, final int timeout) {
		return template.execute(key, new JedisCallback<String>() {
			@Override
			public String call(Jedis jedis) {
				List<String> result = jedis.brpop(timeout, key);
				if(result == null){
					//超时。
					return null;
				}
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public byte[] brpop(final String key, final int timeout) {
		return template.execute(key, new JedisCallback<byte[]>() {
			@Override
			public byte[] call(Jedis jedis) {
				byte[][] oneKey = new byte[][]{key.getBytes(CS_UTF8)};
				List<byte[]> result = jedis.brpop(timeout, oneKey);
				if(result == null){
					//超时。
					return null;
				}
				return result.get(1);
			}
		}, RW.READ);
	}

	@Override
	public List<String> sortString(final String key) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				return jedis.sort(key);
			}
		}, RW.READ);
	}

	@Override
	public List<byte[]> sort(final String key) {
		return template.execute(key, new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> call(Jedis jedis) {
				return jedis.sort(key.getBytes(CS_UTF8));
			}
		}, RW.READ);
	}

	@Override
	public List<String> sortString(final String key, final SortOrder sortOrder) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				SortingParams params = null;
				if(SortOrder.DESC.equals(sortOrder)){
					params = new SortingParams().desc();
				}else{
					params = new SortingParams().asc();
				}
				return jedis.sort(key, params);
			}
		}, RW.READ);
	}

	@Override
	public List<byte[]> sort(final String key, final SortOrder sortOrder) {
		return template.execute(key, new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> call(Jedis jedis) {
				SortingParams params = null;
				if(SortOrder.DESC.equals(sortOrder)){
					params = new SortingParams().desc();
				}else{
					params = new SortingParams().asc();
				}
				return jedis.sort(key.getBytes(CS_UTF8), params);
			}
		}, RW.READ);
	}

	@Override
	public List<String> sortString(final String key, final SortingParams sortingParams) {
		return template.execute(key, new JedisCallback<List<String>>() {
			@Override
			public List<String> call(Jedis jedis) {
				return jedis.sort(key, sortingParams);
			}
		}, RW.READ);
	}

	@Override
	public List<byte[]> sort(final String key, final SortingParams sortingParams) {
		return template.execute(key, new JedisCallback<List<byte[]>>() {
			@Override
			public List<byte[]> call(Jedis jedis) {
				return jedis.sort(key.getBytes(CS_UTF8), sortingParams);
			}
		}, RW.READ);
	}

	@Override
	public long srem(final String key,final String... values) {
		return template.execute(key, new JedisCallback<Long>() {
			@Override
			public Long call(Jedis jedis) {
				if(values == null || values.length == 0){
					throw new IllegalArgumentException("values of srem can't be empty!");
				}
				return jedis.srem(key, values);
			}
		}, RW.WRITE);
	}

	@Override
	public RedisClusterStatistic getStatistic() {

		RedisClusterStatistic statistic = new RedisClusterStatistic();
		List<RedisGroupStatistic> groupStatistics = new ArrayList<RedisGroupStatistic>();
		ShardedGroupJedis groupJedis = template.getShardedJedisGroupPool().getResource();
		if(groupJedis != null){
			for(JedisGroup jedisGroup : groupJedis.getAllShards()){
				RedisGroupStatistic groupStatistic = new RedisGroupStatistic();
				Jedis master = jedisGroup.getMaster();
				if(master != null){
					groupStatistic.setMasterStatistic(RedisStatistic.fromMaster(master.info()));
				}
				List<Jedis> slaves = jedisGroup.getSlaves();
				if(slaves != null && slaves.size() > 0){
					for(Jedis slave : slaves){
						groupStatistic.addSlaveStatistic(RedisStatistic.fromSlave(slave.info()));
					}
				}
				groupStatistics.add(groupStatistic);
			}
		}
		statistic.setGroupStatistics(groupStatistics);
		return statistic;
	}

}
