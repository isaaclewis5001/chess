package dataAccess.auth;

import dataAccess.DatabaseException;
import dataAccess.DatabaseManager;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData getAuthUser(String auth) throws MissingKeyException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                    "SELECT username FROM authTokens WHERE authToken = ?"
            ));
            statement.setString(1, auth);
            ResultSet results = statement.executeQuery();

            if (!results.next()) {
                throw new MissingKeyException("authToken", auth);
            }
            return new AuthData(
                auth,
                results.getString(1)
            );
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void addAuth(AuthData auth) throws DuplicateKeyException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT IGNORE INTO authTokens (authToken, username) VALUES (?, ?)"
            );
            statement.setString(1, auth.authToken());
            statement.setString(2, auth.username());

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new DuplicateKeyException("authToken", auth.authToken());
            }
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void removeAuth(String authToken) throws MissingKeyException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM authTokens WHERE authToken = ?"
            );
            statement.setString(1, authToken);

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new MissingKeyException("authToken", authToken);
            }
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void clear() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM authTokens");
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    public SQLAuthDAO() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                "CREATE TABLE IF NOT EXISTS authTokens (" +
                "   username VARCHAR(64) NOT NULL," +
                "   authToken VARCHAR(64) NOT NULL PRIMARY KEY" +
                ");"
            ));
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }

    }
}
