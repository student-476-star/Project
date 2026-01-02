import java.net.Socket;
import java.util.Random;

public class IDSWorker extends Thread {

    private Socket socket;
    private TrafficProfile profile;

    public IDSWorker(Socket socket, TrafficProfile profile) {
        this.socket = socket;
        this.profile = profile;
    }

    @Override
    public void run() {

        try {

            String ip = socket.getInetAddress().getHostAddress();

            Random random = new Random();

            int accessedPort = 20 + random.nextInt(1000);
            boolean failedLoginAttempt =
                    random.nextInt(10) < 2;

            AttackType attack = IDSRuleEngine.analyzeTraffic(
                    profile,
                    accessedPort,
                    failedLoginAttempt
            );

            if (attack != null) {

                System.out.println(
                        "[ALERT] " + attack +
                        " detected from IP " + ip
                );

                if (IDSConfig.ENABLE_IP_BLOCKING) {
                    IPBlocker.blockIP(ip);
                }
            } else {
                System.out.println(
                        "[INFO] Normal traffic from " + ip
                );
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
