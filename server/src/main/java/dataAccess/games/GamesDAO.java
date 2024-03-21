package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DatabaseException;
import dataAccess.MissingKeyException;
import model.data.GameDesc;

import java.util.List;

/**
 * Data access interface for games
 */
public interface GamesDAO {
    /**
     * @return All previously created games
     */
    List<GameDesc> listGames() throws DatabaseException;

    /**
     * Adds a new game.
     * @param gameName The name of the game to create
     * @return The id of the created game
     */
    int createGame(String gameName) throws DatabaseException;

    /**
     * Adds a player as a participant in a game.
     * @param gameId The ID of the game to modify
     * @param color The team to add the player to
     * @param username The user to add
     * @throws MissingKeyException If the provided gameId was not present
     * @throws BadUpdateException If the provided team was already taken
     */
    void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
            throws MissingKeyException, BadUpdateException, DatabaseException;

    boolean gameExists(int gameId) throws DatabaseException;


    /**
     * Removes all games.
     */
    void clear() throws DatabaseException;
}
