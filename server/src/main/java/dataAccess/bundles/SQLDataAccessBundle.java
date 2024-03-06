package dataAccess.bundles;

import dataAccess.DatabaseException;
import dataAccess.auth.AuthDAO;
import dataAccess.auth.MemoryAuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.games.MemoryGamesDAO;
import dataAccess.user.SQLUserDAO;
import dataAccess.user.UserDAO;
public class SQLDataAccessBundle implements DataAccessBundle {
    private final SQLUserDAO userDAO;
    private final MemoryAuthDAO authDAO;
    private final MemoryGamesDAO gamesDAO;

    @Override
    public AuthDAO auth() {
        return authDAO;
    }

    @Override
    public UserDAO user() {
        return userDAO;
    }

    @Override
    public GamesDAO games() {
        return gamesDAO;
    }

    public SQLDataAccessBundle() throws DatabaseException {
        userDAO = new SQLUserDAO();
        authDAO = new MemoryAuthDAO();
        gamesDAO = new MemoryGamesDAO();
    }
}
