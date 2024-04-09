package service;

import dataAccess.bundles.DataAccessBundle;

public class ServiceBundle {
    private final ClearService clearService;
    private final GamesService gamesService;
    private final JsonService jsonService;
    private final UserService userService;

    private final GameplayService gameplayService;

    public ServiceBundle(DataAccessBundle dataAccessBundle) {
        clearService = new ClearService(dataAccessBundle.auth(), dataAccessBundle.user(), dataAccessBundle.games());
        userService = new UserService(dataAccessBundle.user(), dataAccessBundle.auth());
        jsonService = new JsonService();
        gamesService = new GamesService(dataAccessBundle.games());
        gameplayService = new GameplayService();
    }

    public ClearService clearService() {
        return clearService;
    }

    public UserService userService() {
        return userService;
    }

    public JsonService jsonService() {
        return jsonService;
    }

    public GamesService gamesService() {
        return gamesService;
    }

    public GameplayService gameplayService() { return gameplayService; }
}
