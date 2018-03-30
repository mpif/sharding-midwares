package com.sharding.redis.jedis.statistic;

/**
 * Redis统计量。
 */
public class RedisStatistic {

    public static RedisStatistic fromMaster(String info){
        return null;
    }

    public static RedisStatistic fromSlave(String info){
        return null;
    }

    //角色。
    private String role;

    private ServerStatistic serverStatistic;

    private ClientStatistic clientStatistic;

    private MemoryStatistic memoryStatistic;

    private PersistenceStatistic persistenceStatistic;

    private ReplicationStatistic replicationStatistic;

    private StatsStatistic statsStatistic;

    private KeyspaceStatistic keyspaceStatistic;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ServerStatistic getServerStatistic() {
        return serverStatistic;
    }

    public void setServerStatistic(ServerStatistic serverStatistic) {
        this.serverStatistic = serverStatistic;
    }

    public ClientStatistic getClientStatistic() {
        return clientStatistic;
    }

    public void setClientStatistic(ClientStatistic clientStatistic) {
        this.clientStatistic = clientStatistic;
    }

    public MemoryStatistic getMemoryStatistic() {
        return memoryStatistic;
    }

    public void setMemoryStatistic(MemoryStatistic memoryStatistic) {
        this.memoryStatistic = memoryStatistic;
    }

    public PersistenceStatistic getPersistenceStatistic() {
        return persistenceStatistic;
    }

    public void setPersistenceStatistic(PersistenceStatistic persistenceStatistic) {
        this.persistenceStatistic = persistenceStatistic;
    }

    public ReplicationStatistic getReplicationStatistic() {
        return replicationStatistic;
    }

    public void setReplicationStatistic(ReplicationStatistic replicationStatistic) {
        this.replicationStatistic = replicationStatistic;
    }

    public StatsStatistic getStatsStatistic() {
        return statsStatistic;
    }

    public void setStatsStatistic(StatsStatistic statsStatistic) {
        this.statsStatistic = statsStatistic;
    }

    public KeyspaceStatistic getKeyspaceStatistic() {
        return keyspaceStatistic;
    }

    public void setKeyspaceStatistic(KeyspaceStatistic keyspaceStatistic) {
        this.keyspaceStatistic = keyspaceStatistic;
    }

    @Override
    public String toString() {
        return "RedisStatistic{" +
                "role='" + role + '\'' +
                ", serverStatistic=" + serverStatistic +
                ", clientStatistic=" + clientStatistic +
                ", memoryStatistic=" + memoryStatistic +
                ", persistenceStatistic=" + persistenceStatistic +
                ", replicationStatistic=" + replicationStatistic +
                ", statsStatistic=" + statsStatistic +
                ", keyspaceStatistic=" + keyspaceStatistic +
                '}';
    }

}
