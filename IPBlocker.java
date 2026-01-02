import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IPBlocker {

    private static Map<String, Long> blockedIPs =
            new ConcurrentHashMap<>();

    public static void blockIP(String ip) {

        if (blockedIPs.containsKey(ip)) {
            return;
        }

        blockedIPs.put(ip, System.currentTimeMillis());

        System.out.println(
                "[SECURITY] IP BLOCKED : " + ip +
                " for " + IDSConfig.IP_BLOCK_DURATION_SEC + " seconds"
        );

        if (IDSConfig.ENABLE_LOGGING) {
            IDSLogger.log(
                    ip,
                    "BLOCKED",
                    "IP blocked by IDS engine"
            );
        }
    }

    public static boolean isBlocked(String ip) {

        if (!blockedIPs.containsKey(ip)) {
            return false;
        }

        long blockedAt = blockedIPs.get(ip);
        long elapsedSeconds =
                (System.currentTimeMillis() - blockedAt) / 1000;

        if (elapsedSeconds > IDSConfig.IP_BLOCK_DURATION_SEC) {

            blockedIPs.remove(ip);

            System.out.println(
                    "[SECURITY] IP UNBLOCKED : " + ip
            );

            return false;
        }

        return true;
    }

    public static int getBlockedCount() {
        return blockedIPs.size();
    }
}
