package dataAccess.user;

import dataAccess.DatabaseException;
import dataAccess.DatabaseManager;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    @Override
    public UserData getUserByUsername(String username) throws MissingKeyException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT hashedPassword, email FROM registeredUsers WHERE username = ?");
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            if (!results.next()) {
                throw new MissingKeyException("username", username);
            }
            String password = results.getString(1);
            String email = results.getString(2);
            return new UserData(username, password, email);
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void createUser(UserData user) throws DuplicateKeyException, DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                "INSERT IGNORE INTO registeredUsers (username, hashedPassword, email) VALUES (?, ?, ?)"
            );
            statement.setString(1, user.username());
            statement.setString(2, user.hashedPassword());
            statement.setString(3, user.email());

            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                throw new DuplicateKeyException("username", user.username());
            }
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    @Override
    public void clear() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM registeredUsers");
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }

    public SQLUserDAO() throws DatabaseException {
        try (Connection connection = DatabaseManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement((
                "CREATE TABLE IF NOT EXISTS registeredUsers (" +
                "   username VARCHAR(64) NOT NULL," +
                "   hashedPassword VARCHAR(64) NOT NULL," +
                "   email VARCHAR(64) NOT NULL," +
                "   PRIMARY KEY (username)" +
                ");"
            ));
            statement.execute();
        }
        catch (SQLException exception) {
            throw new DatabaseException(exception.getMessage());
        }
    }
}
