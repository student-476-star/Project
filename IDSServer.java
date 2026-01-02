import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IDSServer {

    private static Map<String, TrafficProfile> trafficDB =
            new ConcurrentHashMap<>();

    private static boolean serverRunning = false;

    public static void startServer() {

        if (serverRunning) {
            System.out.println("[IDS] Server already running");
            return;
        }

        serverRunning = true;

        Thread serverThread = new Thread(() -> {

            try {
                ServerSocket serverSocket =
                        new ServerSocket(IDSConfig.SERVER_PORT);

                System.out.println("======================================");
                System.out.println(" IDS SERVER STARTED");
                System.out.println(" Listening on port : " + IDSConfig.SERVER_PORT);
                System.out.println("======================================");

                while (true) {

                    Socket socket = serverSocket.accept();
                    String clientIP =
                            socket.getInetAddress().getHostAddress();

                    System.out.println(
                            "[CONNECT] Incoming connection from " + clientIP
                    );

                    if (IDSConfig.ENABLE_IP_BLOCKING &&
                            IPBlocker.isBlocked(clientIP)) {

                        System.out.println(
                                "[BLOCKED] Connection rejected from " + clientIP
                        );

                        socket.close();
                        continue;
                    }

                    trafficDB.putIfAbsent(
                            clientIP,
                            new TrafficProfile(clientIP)
                    );

                    IDSWorker worker = new IDSWorker(
                            socket,
                            trafficDB.get(clientIP)
                    );

                    worker.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        serverThread.setDaemon(true);
        serverThread.start();
    }
}
