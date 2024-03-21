package model;

import chess.ChessGame;
import model.data.GameDesc;

public record GameData(
    int gameId,
    String whiteUsername,
    String blackUsername,
    String gameName,
    ChessGame game
) {
    public String getPlayerUsername(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            return whiteUsername;
        }
        return blackUsername;
    }

    public GameData replacePlayer(ChessGame.TeamColor color, String newUsername) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new GameData(gameId, newUsername, blackUsername, gameName, game);
        }
        return new GameData(gameId, whiteUsername, newUsername, gameName, game);
    }

    public GameDesc desc() {
        return new GameDesc(gameId, whiteUsername, blackUsername, gameName);
    }
}
