package dataAccess.user;

import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.UserData;

import java.util.HashMap;

/**
 * An in-memory implementation of UserDAO.
 */
public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userData;
    @Override
    public UserData getUserByUsername(String username) throws MissingKeyException {
        UserData user = userData.get(username);
        if (user == null) {
            throw new MissingKeyException("username", username);
        }
        return user;
    }

    @Override
    public void createUser(UserData user) throws DuplicateKeyException {
        if (userData.containsKey(user.username())) {
            throw new DuplicateKeyException("username", user.username());
        }
        userData.put(user.username(), user);
    }

    @Override
    public void clear() {
        userData.clear();
    }

    public MemoryUserDAO() {
        userData = new HashMap<>();
    }
}
