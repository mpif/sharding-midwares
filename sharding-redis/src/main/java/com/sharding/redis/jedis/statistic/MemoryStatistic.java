package com.sharding.redis.jedis.statistic;

/**
 * Redis内存统计量。
 */
public class MemoryStatistic {

    //已经分配的内存总量。
    private String used;

    //内存使用峰值。
    private String usedPeak;

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUsedPeak() {
        return usedPeak;
    }

    public void setUsedPeak(String usedPeak) {
        this.usedPeak = usedPeak;
    }

    @Override
    public String toString() {
        return "MemoryStatistic{" +
                "used='" + used + '\'' +
                ", usedPeak='" + usedPeak + '\'' +
                '}';
    }

}
