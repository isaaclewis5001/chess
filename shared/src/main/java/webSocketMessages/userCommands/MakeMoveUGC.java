package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveUGC extends UserGameCommand {
    private final ChessMove move;

    public MakeMoveUGC(String authToken, int gameId, ChessMove move) {
        super(authToken, gameId);
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }
    public ChessMove getMove() {
        return move;
    }
}
