import java.io.FileWriter;
import java.io.PrintWriter;

public class IDSReportExporter {

    public static void exportCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(IDSConfig.CSV_REPORT_NAME))) {
            // Export Port Scan Table header
            pw.println("IP,PortScanCount,DOSRequestCount,FailedLogins");
            // This is a placeholder; real data will come from dashboard or TrafficProfile
            pw.println("192.168.1.10,5,10,1");
            pw.println("192.168.1.20,2,8,0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
