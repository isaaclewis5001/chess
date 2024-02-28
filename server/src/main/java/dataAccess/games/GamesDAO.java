package dataAccess.games;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.GameData;
import model.GameDesc;

import java.util.Collection;

/**
 * Data access interface for games
 */
public interface GamesDAO {
    /**
     * @return All previously created games
     */
    Collection<GameDesc> listGames();

    /**
     * Adds a new game.
     * @param gameData The game to create
     * @throws dataAccess.DuplicateKeyException If the game ID is not unique
     */
    void createGame(GameData gameData) throws DuplicateKeyException;

    /**
     * Adds a player as a participant in a game.
     * @param gameId The ID of the game to modify
     * @param color The team to add the player to
     * @param username The user to add
     * @throws MissingKeyException If the provided gameId was not present
     * @throws BadUpdateException If the provided team was already taken
     */
    void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username)
            throws MissingKeyException, BadUpdateException;

    /**
     * @param gameId The ID of the game to fetch
     * @return The game with the specified ID, or null if the game is not present. Will include the game field.
     */
    GameData fetchGame(int gameId) throws MissingKeyException;

    /**
     * Removes all games.
     */
    void clear();
}
