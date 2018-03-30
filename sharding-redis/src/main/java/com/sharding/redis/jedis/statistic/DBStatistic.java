package com.sharding.redis.jedis.statistic;

/**
 * Redis DB统计量。
 * db0:keys=5577,expires=1,avg_ttl=1435681
 */
public class DBStatistic {

    //db下标。
    private int dbIndex;

    //键的数量。
    private int keyCount;

    //带有过期时间的键的数量。
    private int expiredKeyCount;

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public int getExpiredKeyCount() {
        return expiredKeyCount;
    }

    public void setExpiredKeyCount(int expiredKeyCount) {
        this.expiredKeyCount = expiredKeyCount;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    @Override
    public String toString() {
        return "DBStatistic{" +
                "dbIndex=" + dbIndex +
                ", keyCount=" + keyCount +
                ", expiredKeyCount=" + expiredKeyCount +
                '}';
    }

}
