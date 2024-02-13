import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        int actualPort = server.run(15723);
        System.out.println("CS 240 Chess Server running on port " + actualPort);
    }
}