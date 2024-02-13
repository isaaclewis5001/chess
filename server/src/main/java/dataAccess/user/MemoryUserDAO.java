package dataAccess.user;

import model.UserData;

import java.util.HashMap;

/**
 * An in-memory implementation of UserDAO.
 */
public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userData;
    @Override
    public UserData getUserByUsername(String username) {
        return userData.get(username);
    }

    @Override
    public void createUser(UserData data) {
        if (userData.containsKey(data.username())) {
            throw new RuntimeException("Username already exists.");
        }
        userData.put(data.username(), data);
    }

    @Override
    public void clear() {
        userData.clear();
    }

    public MemoryUserDAO() {
        userData = new HashMap<>();
    }
}
