package service;

import com.google.gson.GsonBuilder;
import dataAccess.bundles.DataAccessBundle;

public class ServiceBundle {
    private final ClearService clearService;
    private final GamesService gamesService;
    private final JsonService jsonService;
    private final UserService userService;

    private final WsGameplayService gameplayService;

    public ServiceBundle(DataAccessBundle dataAccessBundle, GsonBuilder gsonBuilder) {
        clearService = new ClearService(dataAccessBundle.auth(), dataAccessBundle.user(), dataAccessBundle.games());
        userService = new UserService(dataAccessBundle.user(), dataAccessBundle.auth());
        jsonService = new JsonService(gsonBuilder);
        gamesService = new GamesService(dataAccessBundle.games());
        gameplayService = new WsGameplayService(userService);
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

    public WsGameplayService gameplayService() { return gameplayService; }
}
