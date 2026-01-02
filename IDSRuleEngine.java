import java.util.*;

public class IDSRuleEngine {

    public static AttackType analyzeTraffic(
            TrafficProfile profile,
            int currentPort,
            boolean failedLoginAttempt) {

        profile.recordRequest();
        profile.recordPortAccess(currentPort);

        if (failedLoginAttempt) {
            profile.recordFailedLogin();
        }

        if (profile.getDurationSeconds() <= IDSConfig.DOS_TIME_WINDOW_SEC &&
                profile.getRequestCount() >= IDSConfig.DOS_REQUEST_LIMIT) {

            return AttackType.DOS_ATTACK;
        }

        if (profile.getPortScanCount() >= IDSConfig.PORT_SCAN_LIMIT) {
            return AttackType.PORT_SCAN;
        }

        if (profile.getFailedLoginCount() >= IDSConfig.BRUTE_FORCE_LIMIT) {
            return AttackType.BRUTE_FORCE;
        }

        if (profile.isIdleTooLong()) {
            profile.resetCounters();
        }

        return null;
    }

    public static String explainAttack(AttackType type) {
        if (type == null) return "Normal Traffic";
        return type.getDescription();
    }
}
