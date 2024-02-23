package dataAccess.auth;

import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.AuthData;

/**
 * Data access interface for auth tokens
 */
public interface AuthDAO {

    /**
     * @param auth The auth token of the
     * @return The UserData corresponding to the given username, or null
     * if none can be found.
     */
    AuthData getAuthUser(String auth) throws MissingKeyException;

    /**
     * Adds a new auth token for a user.
     * @param auth Contains the auth token and user to authorize
     * @throws RuntimeException Throws if the auth token is not unique
     */
    void addAuth(AuthData auth) throws DuplicateKeyException;

    /**
     * Removes the given auth token so that it may no longer be used.
     * @param authToken The auth token to remove
     * @throws MissingKeyException If the auth token does not exist
     */
    void removeAuth(String authToken) throws MissingKeyException;

    /**
     * Deletes all auth tokens.
     */
    void clear();
}
