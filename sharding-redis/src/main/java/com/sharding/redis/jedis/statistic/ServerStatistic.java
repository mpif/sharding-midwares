package com.sharding.redis.jedis.statistic;

/**
 * Redis服务器统计量。
 */
public class ServerStatistic {

    //版本号。
    private String version;

    //Redis服务所在机器操作系统信息。
    private String osInfo;

    //Redis服务进程ID。
    private String pid;

    //Redis服务端口。
    private int port;

    //已经运行多少秒。
    private long uptimeInSeconds;

    //已经运行多少天。
    private long uptimeInDays;

    //配置文件。
    private String configFile;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getUptimeInSeconds() {
        return uptimeInSeconds;
    }

    public void setUptimeInSeconds(long uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    public long getUptimeInDays() {
        return uptimeInDays;
    }

    public void setUptimeInDays(long uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return "ServerStatistic{" +
                "version='" + version + '\'' +
                ", osInfo='" + osInfo + '\'' +
                ", pid='" + pid + '\'' +
                ", port=" + port +
                ", uptimeInSeconds=" + uptimeInSeconds +
                ", uptimeInDays=" + uptimeInDays +
                ", configFile='" + configFile + '\'' +
                '}';
    }

}
