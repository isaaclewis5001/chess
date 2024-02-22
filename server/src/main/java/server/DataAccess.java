package server;

import dataAccess.auth.AuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.user.UserDAO;

public interface DataAccess {
    AuthDAO auth();
    UserDAO user();
    GamesDAO games();
}
