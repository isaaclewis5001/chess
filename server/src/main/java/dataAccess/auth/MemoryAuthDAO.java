package dataAccess.auth;

import dataAccess.DuplicateKeyException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataMap;

    @Override
    public AuthData getAuthUser(String auth) {
        return authDataMap.get(auth);
    }

    @Override
    public void addAuth(AuthData auth) throws DuplicateKeyException {
        if (authDataMap.containsKey(auth.authToken())) {
            throw new DuplicateKeyException("auth token", auth.authToken());
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
