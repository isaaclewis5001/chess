package dataAccess.bundles;

import dataAccess.DataAccessException;
import dataAccess.auth.AuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.user.UserDAO;
public class SQLDataAccessBundle implements DataAccessBundle {

    @Override
    public AuthDAO auth() {
        return null;
    }

    @Override
    public UserDAO user() {
        return null;
    }

    @Override
    public GamesDAO games() {
        return null;
    }

    public SQLDataAccessBundle() throws DataAccessException {

    }
}
