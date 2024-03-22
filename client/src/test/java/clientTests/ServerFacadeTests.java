package clientTests;

import org.junit.jupiter.api.*;
import org.springframework.security.authentication.BadCredentialsException;
import server.Server;
import server.ServerFacade;
import server.UnauthorizedException;
import service.UserService;
import state.LoginState;

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
        LoginState loginState = facade.register("user", "pass", "email");
        Assertions.assertEquals("user", loginState.getUsername());
    }

    @Test
    public void registerNameInUse() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
        facade.register("user", "pass", "email");
        Assertions.assertThrows(UserService.UsernameTakenException.class, () ->
            facade.register("user", "otherPass","otherEmail")
        );
    }

    @Test
    public void login() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
        facade.register("user", "pass", "email");
        LoginState state = facade.login("user", "pass");
        Assertions.assertEquals("user", state.getUsername());
    }

    @Test
    public void loginBadCredentials() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
        facade.register("user", "pass", "email");
        Assertions.assertThrows(BadCredentialsException.class, () -> facade.login("user", "otherPass"));
        Assertions.assertThrows(BadCredentialsException.class, () -> facade.login("otherUser", "pass"));
    }

    @Test
    public void logout() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
        LoginState loginState = facade.register("user", "pass", "email");
        facade.logout(loginState);
        Assertions.assertThrows(UnauthorizedException.class, () -> facade.logout(loginState));
    }
}
