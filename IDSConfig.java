public class IDSConfig {

    public static int SERVER_PORT = 6060;

    public static int DOS_REQUEST_LIMIT = 60;
    public static int DOS_TIME_WINDOW_SEC = 10;

    public static int PORT_SCAN_LIMIT = 12;

    public static int BRUTE_FORCE_LIMIT = 5;

    public static int IP_BLOCK_DURATION_SEC = 120;

    public static int IDLE_RESET_SEC = 30;

    public static boolean ENABLE_LOGGING = true;
    public static boolean ENABLE_IP_BLOCKING = true;

    public static String LOG_FILE_NAME = "ids_log.txt";
    public static String CSV_REPORT_NAME = "ids_report.csv";

    public static void updateDosConfig(int limit, int window) {
        DOS_REQUEST_LIMIT = limit;
        DOS_TIME_WINDOW_SEC = window;
    }

    public static void updatePortScanLimit(int limit) {
        PORT_SCAN_LIMIT = limit;
    }

    public static void updateBruteForceLimit(int limit) {
        BRUTE_FORCE_LIMIT = limit;
    }

    public static void updateBlockDuration(int seconds) {
        IP_BLOCK_DURATION_SEC = seconds;
    }
}
