package dataAccess.bundles;

import dataAccess.auth.AuthDAO;
import dataAccess.auth.MemoryAuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.games.MemoryGamesDAO;
import dataAccess.user.MemoryUserDAO;
import dataAccess.user.UserDAO;

public class MemoryDataAccessBundle implements DataAccessBundle {
    private final AuthDAO auth;
    private final UserDAO user;
    private final GamesDAO games;

    @Override
    public AuthDAO auth() {
        return auth;
    }

    @Override
    public UserDAO user() {
        return user;
    }

    @Override
    public GamesDAO games() {
        return games;
    }

    public MemoryDataAccessBundle() {
        this.auth = new MemoryAuthDAO();
        this.games = new MemoryGamesDAO();
        this.user = new MemoryUserDAO();
    }
}
