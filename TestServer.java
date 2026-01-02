public class TestServer {
    public static void main(String[] args) {
        IDSServer.startServer();
        try { Thread.sleep(600000); } catch (Exception e) {}
    }
}