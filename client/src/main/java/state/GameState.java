package state;

import chess.ChessGame;
import model.data.GameDesc;
import ui.BoardDrawer;
import ui.CommonMessages;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.serverMessages.LoadGameSM;
import webSocketMessages.serverMessages.NotificationSM;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverUGC;
import webSocketMessages.userCommands.JoinPlayerUGC;
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

    private void onLoadGame(LoadGameSM loadGameSM) {
        CommonMessages.wsInterrupt((StringBuilder builder) ->
            BoardDrawer.draw(builder, loadGameSM.getBoard().getBoard(), loadGameSM.getBoard().getTeamToMove(), null)
        );
    }
    private void onError(ErrorSM errorSM) {

    }

    private void onNotify(NotificationSM notificationSM) {

    }
    @Override
    public void accept(ServerMessage serverMessage) {
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                if (serverMessage instanceof LoadGameSM loadGameSM) {
                    onLoadGame(loadGameSM);
                }
            }
            case ERROR -> {
                if (serverMessage instanceof ErrorSM errorSM) {
                    onError(errorSM);
                }
            }
            case NOTIFICATION -> {
                if (serverMessage instanceof NotificationSM notificationSM) {
                    onNotify(notificationSM);
                }
            }
        }
    }
}
