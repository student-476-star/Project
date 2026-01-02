import java.net.Socket;

public class AttackSimulator {

    public static void main(String[] args) {

        String targetIP = "192.168.1.5"; // IDS Laptop IP
        int targetPort = 6060;

        try {

            System.out.println("[ATTACK] Starting DOS simulation...");

            for (int i = 0; i < 200; i++) {
                Socket s = new Socket(targetIP, targetPort);
                s.close();
                Thread.sleep(50);
            }

            System.out.println("[ATTACK] DOS simulation finished");

            System.out.println("[ATTACK] Starting port scan...");

            for (int port = 6000; port < 6020; port++) {
                try {
                    Socket s = new Socket(targetIP, port);
                    s.close();
                } catch (Exception e) {
                }
            }

            System.out.println("[ATTACK] Port scan finished");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
