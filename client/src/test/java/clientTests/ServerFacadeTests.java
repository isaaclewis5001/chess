package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;


public class ServerFacadeTests {

    private static void clearServer() throws Exception {
        URI uri = new URI(serverUrl +"/db");
        HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
        connection.setRequestMethod("DELETE");
        connection.connect();
    }

    private static Server server;

    private static String serverUrl;

    @BeforeAll
    public static void init() throws Exception{
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverUrl = "http://localhost:" + port;
        clearServer();
    }

    @AfterAll
    static void stopServer() {server.stop();}


    @Test
    public void register() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
    }

    @Test
    public void registerNameInUse() {

    }

}
