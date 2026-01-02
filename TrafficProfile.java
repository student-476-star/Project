import java.util.*;

public class TrafficProfile {

    private String ipAddress;

    private int requestCount;
    private int portScanCount;
    private int failedLoginCount;

    private long firstRequestTime;
    private long lastRequestTime;

    private Set<Integer> accessedPorts;

    public TrafficProfile(String ip) {
        this.ipAddress = ip;
        this.requestCount = 0;
        this.portScanCount = 0;
        this.failedLoginCount = 0;
        this.firstRequestTime = System.currentTimeMillis();
        this.lastRequestTime = System.currentTimeMillis();
        this.accessedPorts = new HashSet<>();
    }

    public synchronized void recordRequest() {
        requestCount++;
        lastRequestTime = System.currentTimeMillis();
    }

    public synchronized void recordPortAccess(int port) {
        if (!accessedPorts.contains(port)) {
            accessedPorts.add(port);
            portScanCount++;
        }
    }

    public synchronized void recordFailedLogin() {
        failedLoginCount++;
    }

    public synchronized int getRequestCount() {
        return requestCount;
    }

    public synchronized int getPortScanCount() {
        return portScanCount;
    }

    public synchronized int getFailedLoginCount() {
        return failedLoginCount;
    }

    public synchronized long getDurationSeconds() {
        return (lastRequestTime - firstRequestTime) / 1000;
    }

    public synchronized String getIpAddress() {
        return ipAddress;
    }

    public synchronized void resetCounters() {
        requestCount = 0;
        portScanCount = 0;
        failedLoginCount = 0;
        accessedPorts.clear();
        firstRequestTime = System.currentTimeMillis();
        lastRequestTime = System.currentTimeMillis();
    }

    public synchronized boolean isIdleTooLong() {
        long now = System.currentTimeMillis();
        return (now - lastRequestTime) > (IDSConfig.IDLE_RESET_SEC * 1000);
    }
}
