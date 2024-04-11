package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerUGC extends UserGameCommand {
    private final ChessGame.TeamColor playerColor;
    public JoinPlayerUGC(String authToken, int gameId, ChessGame.TeamColor team) {
        super(authToken, gameId);
        this.playerColor = team;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
