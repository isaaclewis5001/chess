package state;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.data.GameDesc;
import ui.BoardDrawer;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.serverMessages.LoadGameSM;
import webSocketMessages.serverMessages.NotificationSM;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverUGC;
import webSocketMessages.userCommands.JoinPlayerUGC;
import webSocketMessages.userCommands.LeaveUGC;
import webSocketMessages.userCommands.MakeMoveUGC;
import websocket.WebSocketConnection;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.function.Consumer;

public class GameState implements Consumer<ServerMessage> {
    private final WebSocketConnection connection;
    private final int gameId;
    private final LoginState loginState;
    private final ChessGame game;

    private ChessGame.TeamColor color;

    public ChessGame.TeamColor getTeam() {
        return color;
    }

    public GameState(String wsServerURL, LoginState loginState, GameDesc desc) throws IOException, DeploymentException {
        connection = new WebSocketConnection(wsServerURL, this, ServerMessage.class);
        gameId = desc.gameID();
        this.loginState = loginState;
        this.game = new ChessGame();
        this.color = null;
    }

    public void draw(StringBuilder builder, ChessPosition highlightPos) {
        ChessGame.TeamColor perspective = color;
        if (perspective == null) {
            perspective = ChessGame.TeamColor.WHITE;
        }
        BoardDrawer.draw(builder, game.getBoard(), perspective, highlightPos);
    }

    public void join(ChessGame.TeamColor color) throws IOException {
        connection.send(new JoinPlayerUGC(loginState.getAuthToken(), gameId, color));
        this.color = color;
    }

    public void watch() throws IOException {
        connection.send(new JoinObserverUGC(loginState.getAuthToken(), gameId));
    }

    public void leave() throws IOException {
        connection.send(new LeaveUGC(loginState.getAuthToken(), gameId));
    }


    private void onLoadGame(LoadGameSM loadGameSM) {
        this.game.updateBoard(loadGameSM.getBoard());
        StringBuilder builder = new StringBuilder();
        this.draw(builder, null);
        System.out.println(builder);
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

    public ChessGame getGame() {
        return game;
    }

    public void move(ChessMove move) throws IOException {
        connection.send(new MakeMoveUGC(loginState.getAuthToken(), gameId, move));
    }
}
