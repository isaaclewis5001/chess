package dataAccess.user;

import dataAccess.DatabaseException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.UserData;


/**
 * User data access interface.
 */
public interface UserDAO {
    /**
     * @param username The username to query
     * @return The UserData corresponding to the given username
     * @throws MissingKeyException When no user with the username exists
     */
    UserData getUserByUsername(String username) throws MissingKeyException, DatabaseException;

    /**
     * Creates a new user with the provided UserData
     * @param user Describes the user to create
     * @throws DuplicateKeyException Throws when the provided username already exists
     */
    void createUser(UserData user) throws DuplicateKeyException, DatabaseException;

    /**
     * Deletes all user data.
     */
    void clear() throws DatabaseException;
}
