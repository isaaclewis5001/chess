package webSocketMessages.serverMessages;

import chess.ChessBoard;

public class LoadGameSM extends ServerMessage {
    private final ChessBoard game;
    public LoadGameSM(ChessBoard board) {
        super(ServerMessageType.LOAD_GAME);
        game = board;
    }
    public ChessBoard getBoard() {
        return game;
    }
}
