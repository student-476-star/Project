public enum AttackType {

    DOS_ATTACK("Denial of Service Attack"),
    PORT_SCAN("Port Scanning Attack"),
    BRUTE_FORCE("Brute Force Login Attack");

    private final String description;

    AttackType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AttackType fromString(String type) {
        for (AttackType a : values()) {
            if (a.name().equalsIgnoreCase(type)) {
                return a;
            }
        }
        return null;
    }
}
