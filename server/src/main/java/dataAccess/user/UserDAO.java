package dataAccess.user;

import model.UserData;


/**
 * User data access interface.
 */
public interface UserDAO {
    /**
     * @param username The username to query
     * @return The UserData corresponding to the given username, or null
     * if none can be found.
     */
    UserData getUserByUsername(String username);

    /**
     * Creates a new user with the provided UserData
     * @param user Describes the user to create
     * @throws RuntimeException Throws when the provided username already exists
     */
    void createUser(UserData user);

    /**
     * Deletes all user data.
     */
    void clear();
}
