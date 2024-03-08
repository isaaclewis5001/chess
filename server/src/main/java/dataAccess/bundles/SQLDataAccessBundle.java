package dataAccess.bundles;

import dataAccess.DatabaseException;
import dataAccess.auth.AuthDAO;
import dataAccess.auth.SQLAuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.games.SQLGamesDAO;
import dataAccess.user.SQLUserDAO;
import dataAccess.user.UserDAO;
public class SQLDataAccessBundle implements DataAccessBundle {
    private final SQLUserDAO userDAO;
    private final SQLAuthDAO authDAO;
    private final SQLGamesDAO gamesDAO;

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

    public SQLDataAccessBundle() {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gamesDAO = new SQLGamesDAO();
        } catch (DatabaseException ex) {
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
}
