package dataAccess.games;

import chess.ChessGame;
import dataAccess.*;
import model.GameDesc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGamesDAO implements GamesDAO {
    @Override
    public Collection<GameDesc> listGames() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                    "SELECT gameId, whiteUsername, blackUsername, gameName FROM gamesDesc"
            ));
            ResultSet results = statement.executeQuery();

            ArrayList<GameDesc> games = new ArrayList<>();
            while(results.next()) {
                GameDesc desc = new GameDesc(
                    results.getInt(1),
                    results.getString(2),
                    results.getString(3),
                    results.getString(4)
                );
                games.add(desc);
            }
            return games;
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                "INSERT INTO gamesDesc (gameName) VALUES (?);"
            ), PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, gameName);
            statement.executeUpdate();
            ResultSet keyResults = statement.getGeneratedKeys();
            if (!keyResults.next()) {
                throw new DatabaseException("Query did not return auto incremented id");
            }
            return keyResults.getInt(1);
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void updateGameParticipants(int gameId, ChessGame.TeamColor color, String username) throws MissingKeyException, BadUpdateException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement;
            if (color == ChessGame.TeamColor.WHITE) {
                statement = connection.prepareStatement((
                    "UPDATE gamesDesc SET whiteUsername = ? WHERE gameId = ? AND whiteUsername IS NULL"
                ));
            }
            else {
                statement = connection.prepareStatement((
                    "UPDATE gamesDesc SET blackUsername = ? WHERE gameId = ? AND blackUsername IS NULL"
                ));
            }
            statement.setString(1, username);
            statement.setInt(2, gameId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                if (gameExists(gameId)) {
                    throw new BadUpdateException("team taken");
                }
                else {
                    throw new MissingKeyException("gameId", String.valueOf(gameId));
                }
            }
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public boolean gameExists(int gameId) throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                "SELECT NULL FROM gamesDesc WHERE gameId = ?;"
            ));
            statement.setInt(1, gameId);
            ResultSet results = statement.executeQuery();
            return results.next();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }


    @Override
    public void clear() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM gamesDesc;"
            );
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    public SQLGamesDAO() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                "CREATE TABLE IF NOT EXISTS gamesDesc (" +
                "   gameId int NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "   whiteUsername VARCHAR(64)," +
                "   blackUsername VARCHAR(64)," +
                "   gameName VARCHAR(64) NOT NULL" +
                ");"
            ));
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }
}
