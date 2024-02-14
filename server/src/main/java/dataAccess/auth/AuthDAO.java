package dataAccess.auth;

import model.AuthData;

public interface AuthDAO {

    /**
     * @param auth The auth token of the
     * @return The UserData corresponding to the given username, or null
     * if none can be found.
     */
    AuthData getAuthUser(String auth);

    /**
     * Adds a new auth token for a user.
     * @param auth Contains the auth token and user to authorize
     * @throws RuntimeException Throws if the auth token is not unique
     */
    void addAuth(AuthData auth);

    /**
     * Removes the given auth token so that it may no longer be used, if it exists.
     * @param authToken The auth token to remove
     */
    void removeAuth(String authToken);

    /**
     * Deletes all auth tokens.
     */
    void clear();
}
