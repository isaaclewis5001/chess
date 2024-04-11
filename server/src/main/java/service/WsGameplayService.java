package service;

import dataAccess.DatabaseException;
import dataAccess.DuplicateKeyException;
import model.WsClient;
import model.WsGame;
import model.data.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.HashMap;

public class WsGameplayService {
    private final HashMap<String, WsClient> clients;

    private final HashMap<Integer, WsGame> games;

    private final UserService userService;

    public WsGameplayService(UserService userService) {
        clients = new HashMap<>();
        games = new HashMap<>();
        this.userService = userService;
    }

    public WsClient addClient(Session session, UserGameCommand command) throws DuplicateKeyException, DatabaseException, UserService.BadAuthException {
        if (clients.containsKey(command.getAuthString())) {
            throw new DuplicateKeyException("authToken", command.getAuthString());
        }

        AuthData auth = userService.getAuthUser(command.getAuthString());

        WsClient client = new WsClient(command.getAuthString(), auth.username(), session, command.getGameID());
        clients.put(client.authToken(), client);
        return client;
    }

    public void removeClient(WsClient client) {
        clients.remove(client.authToken(), client);
        WsGame game = games.get(client.gameId());
        if (game != null) {
            game.removeClient(client);
        }
    }

    public WsGame getGame(int id) {
        return games.get(id);
    }

    public WsGame getOrCreateGame(int id) {
        return games.computeIfAbsent(id, WsGame::new);
    }

    public WsClient getClient(String authToken) {
        return clients.get(authToken);
    }
}
