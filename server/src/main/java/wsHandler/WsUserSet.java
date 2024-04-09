package wsHandler;

import dataAccess.DuplicateKeyException;

import java.util.HashMap;

public class WsUserSet {
    private final HashMap<String, WsClient> clients;

    private final HashMap<Integer, WsGame> games;

    public WsUserSet() {
        clients = new HashMap<>();
        games = new HashMap<>();
    }

    public void addClient(WsClient client) throws DuplicateKeyException {
        if (clients.containsKey(client.authToken())) {
            throw new DuplicateKeyException("authToken", client.authToken());
        }
        clients.put(client.authToken(), client);
    }

    public WsGame getGame(int id) {
        return games.computeIfAbsent(id, WsGame::new);
    }

    public WsClient getClient(String authToken) {
        return clients.get(authToken);
    }
}
