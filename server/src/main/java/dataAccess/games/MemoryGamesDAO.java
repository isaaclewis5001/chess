package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.MissingKeyException;
import model.GameData;
import model.GameDesc;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGamesDAO implements GamesDAO {
    private final ArrayList<GameData> games;

    @Override
    public Collection<GameDesc> listGames() {
        return games.stream().map(GameData::desc).toList();
    }

    @Override
    public int createGame(String gameName) {
        int newId = games.size() + 1;
        games.add(new GameData(newId, null, null, gameName, null));
        return newId;
    }

    @Override
    public void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
        throws MissingKeyException, BadUpdateException {

        if (!gameExists(gameId)) {
            throw new MissingKeyException("gameId", String.valueOf(gameId));
        }
        GameData oldGame = games.get(gameId - 1);

        if (oldGame.getPlayerUsername(color) != null) {
            throw new BadUpdateException("username already present");
        }
        GameData newGame = oldGame.replacePlayer(color, username);
        games.set(gameId - 1, newGame);
    }

    @Override
    public boolean gameExists(int gameId) {
        return gameId <= games.size() && gameId > 0;
    }

    @Override
    public void clear() {
        games.clear();
    }

    public MemoryGamesDAO() {
        games = new ArrayList<>();
    }
}
