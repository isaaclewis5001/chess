package server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import model.data.AuthData;
import model.request.LoginRequest;
import state.LoginState;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final URL sessionURL;

    private final Gson gson;

    public ServerFacade(String serverURL) throws IOException {
        this.sessionURL = URI.create(serverURL + "/session").toURL();
        this.gson = new Gson();
    }


    private static HttpURLConnection getConnection(URL url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(3000);
        connection.setRequestMethod(method);
        return connection;
    }
    private <T> int execute(HttpURLConnection connection, T requestObject) throws IOException {
        connection.setDoOutput(true);
        connection.getOutputStream().write(gson.toJson(requestObject).getBytes());
        connection.connect();
        return connection.getResponseCode();
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


}
