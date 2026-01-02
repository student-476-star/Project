import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IDSLogger {

    public static synchronized void log(String ip, String attack, String msg) {
        try (FileWriter fw = new FileWriter(IDSConfig.LOG_FILE_NAME, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String time = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String logEntry = time + " | IP: " + ip + " | Attack: " + attack + " | Msg: " + msg;

            pw.println(logEntry);

            System.out.println("[LOG] " + logEntry);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
