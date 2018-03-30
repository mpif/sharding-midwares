package com.sharding.redis.jedis.statistic;

import com.sharding.redis.jedis.statistic.DBStatistic;

import java.util.List;

/**
 * Redis键空间统计量。
 */
public class KeyspaceStatistic {

    //DB统计量列表。
    private List<DBStatistic> dbStatistics;

    public List<DBStatistic> getDbStatistics() {
        return dbStatistics;
    }

    public void setDbStatistics(List<DBStatistic> dbStatistics) {
        this.dbStatistics = dbStatistics;
    }

    @Override
    public String toString() {
        return "KeyspaceStatistic{" +
                "dbStatistics=" + dbStatistics +
                '}';
    }

}
