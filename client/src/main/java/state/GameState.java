package state;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import model.data.GameDesc;
import ui.BoardDrawer;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.serverMessages.LoadGameSM;
import webSocketMessages.serverMessages.NotificationSM;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import websocket.WebSocketConnection;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.function.Consumer;

public class GameState implements Consumer<ServerMessage> {
    private final WebSocketConnection connection;
    private final int gameId;
    private final LoginState loginState;
    private final ChessGame game;

    private final Gson gson = new Gson();
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

    public void draw(StringBuilder builder, ChessPosition highlightPos, ChessGame.TeamColor highlightTeam) {
        ChessGame.TeamColor perspective = color;
        if (perspective == null) {
            perspective = ChessGame.TeamColor.WHITE;
        }
        BoardDrawer.draw(builder, game.getBoardState(), perspective, highlightPos, highlightTeam);
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
        this.draw(builder, null, null);
        System.out.println(builder);
        if (game.isInCheckmate(game.getTeamTurn())) {
            System.out.println("Check!");
        }
        else if (game.isInCheck(game.getTeamTurn())) {
            System.out.println("Checkmate!");
            System.out.println(game.getTeamTurn().opponent() + " wins!");
        }
        else if (game.isInStalemate(game.getTeamTurn())) {
            System.out.println("Stalemate!");
        }
    }
    private void onError(ErrorSM errorSM) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Server error:");
        System.out.println(errorSM.getErrorMessage());
    }

    private void onNotify(NotificationSM notificationSM) {
        String[] segments = notificationSM.getMessage().split(" ", 2);
        if (segments.length == 0) {
            return;
        }
        String base = segments[0];
        switch (base) {
            case "join" -> {
                String[] subSegments = segments[1].split(" ", 2);
                if (subSegments.length == 2) {
                    System.out.println(subSegments[1] + " joined as " + subSegments[0]);
                }
            }
            case "obs" ->
                System.out.println(segments[1] + " is now watching");
            case "move" -> {
                ChessMove move;
                try {
                    move = gson.fromJson(segments[1], ChessMove.class);
                } catch (JsonParseException ex) { return; }
                System.out.println("Move made: " + move.getStartPosition() + " to " + move.getEndPosition());
            }
            case "leave" ->
                System.out.println(segments[1] + " left.");
            case "resign" -> {
                game.setGameOver();
                System.out.println("Resignation: " + segments[1] + " resigned.");
            }

        }

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

    public void resign() throws IOException {
        connection.send(new ResignUGC(loginState.getAuthToken(), gameId));
    }
}
