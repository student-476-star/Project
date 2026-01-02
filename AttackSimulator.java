import java.util.Random;

public class AttackSimulator {

    public static void startSimulation(IDSDashboard dashboard) {
        Random random = new Random();
        String[] ips = {"192.168.1.10","192.168.1.20","192.168.1.30","192.168.1.40"};

        while(true) {
            try {
                Thread.sleep(1000); // every 1 sec

                String ip = ips[random.nextInt(ips.length)];
                int portScanCount = random.nextInt(10);
                int dosCount = random.nextInt(20);

                boolean attackDetected = random.nextInt(10) < 3;

                // Update dashboard
                dashboard.updatePortScan(ip, portScanCount);
                dashboard.updateDOS(ip, dosCount);

                if(attackDetected) {
                    dashboard.addAlert(ip, "Port Scan/DOS Detected");

                    // Block IP sometimes
                    if(random.nextBoolean()) {
                        IPBlocker.blockIP(ip);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
