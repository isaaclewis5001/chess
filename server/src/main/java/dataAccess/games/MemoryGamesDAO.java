package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.MissingKeyException;
import model.data.GameDesc;

import java.util.ArrayList;
import java.util.List;

public class MemoryGamesDAO implements GamesDAO {
    private final ArrayList<GameDesc> games;

    @Override
    public List<GameDesc> listGames() {
        return games;
    }

    @Override
    public int createGame(String gameName) {
        int newId = games.size() + 1;
        games.add(new GameDesc(newId, null, null, gameName));
        return newId;
    }

    @Override
    public void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
        throws MissingKeyException, BadUpdateException {

        if (!gameExists(gameId)) {
            throw new MissingKeyException("gameId", String.valueOf(gameId));
        }
        GameDesc oldGame = games.get(gameId - 1);

        if (oldGame.getPlayerUsername(color) != null) {
            throw new BadUpdateException("username already present");
        }
        GameDesc newGame = oldGame.replacePlayer(color, username);
        games.set(gameId - 1, newGame);
    }

    @Override
    public boolean gameExists(int gameId) {
        return gameId <= games.size() && gameId > 0;
    }

    @Override
    public GameDesc fetchGame(int gameId) throws MissingKeyException {
        if (!gameExists(gameId)) {
            throw new MissingKeyException("gameID", String.valueOf(gameId));
        }
        return games.get(gameId);
    }

    @Override
    public void clear() {
        games.clear();
    }

    public MemoryGamesDAO() {
        games = new ArrayList<>();
    }
}
