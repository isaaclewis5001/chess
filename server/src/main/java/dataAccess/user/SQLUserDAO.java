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

    }

    @Override
    public void clear() {

    }
}
