package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.GameData;
import model.GameDesc;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGamesDAO implements GamesDAO {
    private final HashMap<Integer, GameData> games;

    @Override
    public Collection<GameDesc> listGames() {
        return games.values().stream().map(GameData::desc).toList();
    }

    @Override
    public void createGame(GameData gameData) throws DuplicateKeyException {
        if (games.containsKey(gameData.gameId())) {
            throw new DuplicateKeyException("game ID", String.valueOf(gameData.gameId()));
        }
        games.put(gameData.gameId(), gameData);
    }

    @Override
    public void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
        throws MissingKeyException, BadUpdateException {

        GameData oldGame = fetchGame(gameId);

        if (oldGame.getPlayerUsername(color) != null) {
            throw new BadUpdateException("username already present");
        }
        GameData newGame = oldGame.replacePlayer(color, username);
        games.put(gameId, newGame);
    }

    @Override
    public GameData fetchGame(int gameId) throws MissingKeyException {
        GameData result = games.get(gameId);
        if (result == null) {
            throw new MissingKeyException("game ID", String.valueOf(gameId));
        }
        return result;
    }

    @Override
    public void clear() {
        games.clear();
    }

    public MemoryGamesDAO() {
        games = new HashMap<>();
    }
}
