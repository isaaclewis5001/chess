package state;

import chess.ChessGame;
import model.data.GameDesc;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverUGC;
import webSocketMessages.userCommands.JoinPlayerUGC;
import webSocketMessages.userCommands.UserGameCommand;
import websocket.WebSocketConnection;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.function.Consumer;

public class GameState implements Consumer<ServerMessage> {
    private final WebSocketConnection connection;

    private final int gameId;

    private final LoginState loginState;

    public GameState(String wsServerURL, LoginState loginState, GameDesc desc) throws IOException, DeploymentException {
        connection = new WebSocketConnection(wsServerURL, this, ServerMessage.class);
        gameId = desc.gameID();
        this.loginState = loginState;
    }

    public void join(ChessGame.TeamColor color) throws IOException {
        connection.send(new JoinPlayerUGC(loginState.getAuthToken(), gameId, color));
    }

    public void watch() throws IOException {
        connection.send(new JoinObserverUGC(loginState.getAuthToken(), gameId));
    }


    @Override
    public void accept(ServerMessage serverMessage) {

    }
}
