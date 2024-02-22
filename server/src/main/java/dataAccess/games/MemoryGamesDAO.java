package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGamesDAO implements GamesDAO {
    private final HashMap<Integer, GameData> games;

    @Override
    public Collection<GameData> listGames() {
        return games.values().stream().map(GameData::withoutGameState).toList();
    }

    @Override
    public void createGame(GameData gameData) throws DuplicateKeyException {
        if (games.containsKey(gameData.gameId())) {
            throw new DuplicateKeyException("game ID", String.valueOf(gameData.gameId()));
        }
        games.put(gameData.gameId(), gameData);
    }

    @Override
    public void removeGame(int gameId) {
        games.remove(gameId);
    }

    @Override
    public void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
        throws MissingKeyException, BadUpdateException {

        GameData oldGame = games.get(gameId);
        if (oldGame == null) {
            throw new MissingKeyException("game ID", String.valueOf(gameId));
        }

        if (!oldGame.getPlayerUsername(color).isEmpty()) {
            throw new BadUpdateException("username already present");
        }
        games.put(gameId, oldGame.replacePlayer(color, username));
    }

    @Override
    public GameData fetchGame(int gameId) {
        return games.get(gameId);
    }

    @Override
    public void clear() {
        games.clear();
    }

    public MemoryGamesDAO() {
        games = new HashMap<>();
    }
}
