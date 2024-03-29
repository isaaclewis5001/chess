package dataAccess.auth;

import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import model.data.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authDataMap;

    @Override
    public AuthData getAuthUser(String auth) throws MissingKeyException {
        AuthData result = authDataMap.get(auth);
        if (result == null) {
            throw new MissingKeyException("username", auth);
        }
        return result;
    }

    @Override
    public void addAuth(AuthData auth) throws DuplicateKeyException {
        if (authDataMap.containsKey(auth.authToken())) {
            throw new DuplicateKeyException("auth token", auth.authToken());
        }
        authDataMap.put(auth.authToken(), auth);
    }

    @Override
    public void removeAuth(String authToken) throws MissingKeyException {
        if (authDataMap.remove(authToken) == null) {
            throw new MissingKeyException("auth token", authToken);
        }
    }

    @Override
    public void clear() {
        authDataMap.clear();
    }


    public MemoryAuthDAO() {
        this.authDataMap = new HashMap<>();
    }
}
