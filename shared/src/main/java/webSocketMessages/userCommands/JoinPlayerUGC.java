package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerUGC extends UserGameCommand {
    private final ChessGame.TeamColor team;
    public JoinPlayerUGC(String authToken, int gameId, ChessGame.TeamColor team) {
        super(authToken, gameId);
        this.team = team;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public ChessGame.TeamColor getTeam() {
        return team;
    }
}
