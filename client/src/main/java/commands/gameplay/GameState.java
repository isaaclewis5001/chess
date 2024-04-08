package commands.gameplay;

import webSocketMessages.serverMessages.ServerMessage;
import websocket.WebSocketConnection;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.function.Consumer;

public class GameState implements Consumer<ServerMessage> {
    private final WebSocketConnection connection;

    public GameState(String wsServerURL) throws IOException, DeploymentException {
        connection = new WebSocketConnection(wsServerURL, this, ServerMessage.class);
    }

    @Override
    public void accept(ServerMessage serverMessage) {

    }
}
