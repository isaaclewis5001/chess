package webSocketMessages.serverMessages;

import chess.BoardState;

public class LoadGameSM extends ServerMessage {
    private final BoardState game;
    public LoadGameSM(BoardState board) {
        super(ServerMessageType.LOAD_GAME);
        game = board;
    }
    public BoardState getBoard() {
        return game;
    }
}
