package clientTests;

import chess.ChessGame;
import model.data.GameDesc;
import org.junit.jupiter.api.*;
import server.*;
import state.LoginState;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class ServerFacadeTests {

    private static void clearServer() throws Exception {
        URI uri = new URI(serverUrl +"/db");
        HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
        connection.setRequestMethod("DELETE");
        connection.connect();
        Assertions.assertEquals(200, connection.getResponseCode());
    }

    private static Server server;

    private static String serverUrl;

    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception{
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    public void initEach() throws Exception {
        clearServer();
    }

    @AfterAll
    static void stopServer() {server.stop();}


    @Test
    public void register() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");
        Assertions.assertEquals("user", loginState.getUsername());
    }

    @Test
    public void registerNameInUse() throws Exception {
        facade.register("user", "pass", "email");
        Assertions.assertThrows(UsernameInUseException.class, () ->
            facade.register("user", "otherPass","otherEmail")
        );
    }

    @Test
    public void login() throws Exception {
        facade.register("user", "pass", "email");
        LoginState state = facade.login("user", "pass");
        Assertions.assertEquals("user", state.getUsername());
    }

    @Test
    public void loginBadCredentials() throws Exception {
        ServerFacade facade = new ServerFacade(serverUrl);
        facade.register("user", "pass", "email");
        Assertions.assertThrows(LoginException.class, () -> facade.login("user", "otherPass"));
        Assertions.assertThrows(LoginException.class, () -> facade.login("otherUser", "pass"));
    }

    @Test
    public void logout() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");
        Assertions.assertTrue(facade.logout(loginState));
        Assertions.assertFalse(facade.logout(loginState));
    }

    @Test
    public void createGame() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");
        facade.createGame(loginState, "game1");
        facade.createGame(loginState,"game2");
        facade.createGame(loginState, "game3");
    }

    @Test
    public void addGameUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> facade.createGame(new LoginState("not", "allowed"), "game"));
    }

    @Test
    public void listGames() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");

        facade.createGame(loginState, "game1");
        facade.createGame(loginState,"game2");
        facade.createGame(loginState, "game3");

        Set<String> names = facade.listGames(loginState).stream().map(GameDesc::gameName).collect(Collectors.toSet());
        Set<String> expect = new HashSet<>();
        expect.add("game1");
        expect.add("game2");
        expect.add("game3");

        Assertions.assertEquals(expect, names);
    }

    @Test
    public void listGamesUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> facade.listGames(new LoginState("not", "allowed")));
    }

    @Test
    public void joinGame() throws Exception {
        LoginState loginState1 = facade.register("user1", "pass1", "email1");
        LoginState loginState2 = facade.register("user2", "pass2", "email2");
        LoginState loginState3 = facade.register("user3", "pass3", "email3");
        facade.createGame(loginState1, "game");
        GameDesc descInit = facade.listGames(loginState1).getFirst();
        facade.joinGame(loginState1, descInit.gameID(), ChessGame.TeamColor.WHITE);
        facade.joinGame(loginState2, descInit.gameID(), ChessGame.TeamColor.BLACK);
        facade.joinGame(loginState3, descInit.gameID(), null);

        GameDesc descFinal = facade.listGames(loginState1).getFirst();
        Assertions.assertEquals(new GameDesc(descInit.gameID(), "user1", "user2", "game"), descFinal);
    }

    @Test
    public void joinGameUnauthorized() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");
        facade.createGame(loginState, "game");
        int id = facade.listGames(loginState).getFirst().gameID();
        LoginState spoof = new LoginState("not", "authorized");
        Assertions.assertThrows(UnauthorizedException.class, () -> facade.joinGame(spoof, id, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(UnauthorizedException.class, () -> facade.joinGame(spoof, id, null));
    }

    @Test
    public void joinAbsentGame() throws Exception {
        LoginState loginState = facade.register("user", "pass", "email");
        Assertions.assertThrows(BadGameIdException.class, () -> facade.joinGame(loginState, 0, ChessGame.TeamColor.WHITE));
        Assertions.assertThrows(BadGameIdException.class, () -> facade.joinGame(loginState, 0, null));
    }

    @Test
    public void joinBadTeam() throws Exception {
        LoginState loginState1 = facade.register("user1", "pass1", "email1");
        LoginState loginState2 = facade.register("user2", "pass2", "email2");
        facade.createGame(loginState1, "game");
        int id = facade.listGames(loginState1).getFirst().gameID();
        facade.joinGame(loginState1, id, ChessGame.TeamColor.WHITE);
        Assertions.assertThrows(TeamTakenException.class, () -> facade.joinGame(loginState2, id, ChessGame.TeamColor.WHITE));
    }
}
