package dataAccess.bundles;

import dataAccess.auth.AuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.user.UserDAO;

public interface DataAccessBundle {
    AuthDAO auth();
    UserDAO user();
    GamesDAO games();
}
