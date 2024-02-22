package service;

import dataAccess.auth.AuthDAO;
import dataAccess.games.GamesDAO;
import dataAccess.user.UserDAO;

public class ClearService {
    private final AuthDAO auth;
    private final UserDAO users;
    private final GamesDAO games;
    public void clear() {
        auth.clear();
        users.clear();
        games.clear();
    }

    public ClearService(AuthDAO auth, UserDAO users, GamesDAO games) {
        this.auth = auth;
        this.users = users;
        this.games = games;
    }
}
