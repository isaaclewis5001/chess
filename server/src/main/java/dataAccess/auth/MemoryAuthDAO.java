package dataAccess.auth;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataMap;

    @Override
    public AuthData getAuthUser(String auth) {
        return authDataMap.get(auth);
    }

    @Override
    public void addAuth(AuthData auth) {
        if (authDataMap.containsKey(auth.authToken())) {
            throw new RuntimeException("Auth token was not unique.");
        }
        authDataMap.put(auth.authToken(), auth);
    }

    @Override
    public void removeAuth(String authToken) {
        authDataMap.remove(authToken);
    }

    @Override
    public void clear() {
        authDataMap.clear();
    }


    public MemoryAuthDAO() {
        this.authDataMap = new HashMap<>();
    }
}
