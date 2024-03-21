package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import model.data.AuthData;
import model.data.GameDesc;
import model.request.CreateGameRequest;
import model.request.CreateUserRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.response.ListGamesResponse;
import state.LoginState;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class ServerFacade {
    private final URL userURL;
    private final URL sessionURL;
    private final URL gamesURL;
    private final Gson gson;

    public ServerFacade(String serverURL) throws IOException {
        this.userURL = URI.create(serverURL + "/user").toURL();
        this.sessionURL = URI.create(serverURL + "/session").toURL();
        this.gamesURL = URI.create(serverURL + "/game").toURL();
        this.gson = new Gson();
    }


    private static HttpURLConnection getConnection(URL url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(3000);
        connection.setRequestMethod(method);
        return connection;
    }

    private int execute(HttpURLConnection connection) throws IOException {
        connection.connect();
        return connection.getResponseCode();
    }
    private <T> int execute(HttpURLConnection connection, T requestObject) throws IOException {
        connection.setDoOutput(true);
        connection.getOutputStream().write(gson.toJson(requestObject).getBytes());
        return execute(connection);
    }
    private <T> T interpretResponseBody(HttpURLConnection connection, Class<T> tClass) throws IOException, ServerException {
        try {
            return gson.fromJson(new InputStreamReader(connection.getInputStream()), tClass);
        } catch (JsonParseException ex) {
            throw new ServerException("Malformed server response");
        }
    }

    private static ServerException generalFailure(int code) {
        return new ServerException("Server failed with status code " + code);
    }

    private static void insertAuthInfo(HttpURLConnection connection, LoginState login) {
        connection.setRequestProperty("authorization", login.getAuthToken());
    }

    public LoginState login(String username, String password) throws IOException, LoginException, ServerException {
        HttpURLConnection connection = getConnection(sessionURL, "POST");

        LoginRequest request = new LoginRequest(username, password);

        int code = execute(connection, request);

        if (code == 200) {
            AuthData authData = interpretResponseBody(connection, AuthData.class);
            return new LoginState(authData.username(), authData.authToken());
        }
        else if (code == 401) {
            throw new LoginException();
        }
        else {
            throw generalFailure(code);
        }
    }


    public boolean logout(LoginState loginState) {
        try {
            HttpURLConnection connection = getConnection(sessionURL, "DELETE");
            insertAuthInfo(connection, loginState);
            int code = execute(connection);
            return code == 200;
        } catch (Exception ex) {
            return false;
        }
    }

    public LoginState register(String username, String password, String email) throws IOException, ServerException, UsernameInUseException {
        HttpURLConnection connection = getConnection(userURL, "POST");

        CreateUserRequest request = new CreateUserRequest(username, password, email);

        int code = execute(connection, request);

        if (code == 200) {
            AuthData authData = interpretResponseBody(connection, AuthData.class);
            return new LoginState(authData.username(), authData.authToken());
        }
        else if (code == 403) {
            throw new UsernameInUseException();
        }
        else {
            throw generalFailure(code);
        }
    }

    public void createGame(LoginState loginState, String name) throws IOException, ServerException, UnauthorizedException {
        HttpURLConnection connection = getConnection(gamesURL, "POST");
        insertAuthInfo(connection, loginState);

        CreateGameRequest request = new CreateGameRequest(name);
        int code = execute(connection, request);
        if (code == 401) {
            throw new UnauthorizedException();
        }
        else if (code != 200) {
            throw generalFailure(code);
        }
    }

    public List<GameDesc> listGames(LoginState loginState) throws IOException, ServerException, UnauthorizedException {
        HttpURLConnection connection = getConnection(gamesURL, "GET");
        insertAuthInfo(connection, loginState);

        int code = execute(connection);
        if (code == 200) {
            return interpretResponseBody(connection, ListGamesResponse.class).games();
        }
        else if (code == 401) {
            throw new UnauthorizedException();
        }
        else {
            throw generalFailure(code);
        }
    }

    public void joinGame(LoginState loginState, int gameId, ChessGame.TeamColor team)
            throws IOException, ServerException, UnauthorizedException, TeamTakenException, BadGameIdException {
        HttpURLConnection connection = getConnection(gamesURL, "PUT");
        insertAuthInfo(connection, loginState);

        int code = execute(connection, new JoinGameRequest(gameId, team));
        if (code == 401) {
            throw new UnauthorizedException();
        }
        else if (code == 403) {
            throw new TeamTakenException();
        }
        else if (code == 400) {
            throw new BadGameIdException();
        }
        else if (code != 200) {
            throw generalFailure(code);
        }
    }
}
