package model.data;

import chess.ChessGame;

public record GameDesc(int gameID, String whiteUsername, String blackUsername, String gameName) {
    public String getPlayerUsername(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return whiteUsername;
        }
        else {
            return blackUsername;
        }
    }

    public GameDesc replacePlayer(ChessGame.TeamColor color, String username) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new GameDesc(gameID, username, blackUsername, gameName);
        }
        return new GameDesc(gameID, whiteUsername, username, gameName);
    }
}
