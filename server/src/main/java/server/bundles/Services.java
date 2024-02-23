package server.bundles;

import service.ClearService;
import service.GamesService;
import service.JsonService;
import service.UserService;

public class Services {
    private final ClearService clearService;
    private final GamesService gamesService;
    private final JsonService jsonService;
    private final UserService userService;

    public Services(DataAccess dataAccess) {
        clearService = new ClearService(dataAccess.auth(), dataAccess.user(), dataAccess.games());
        userService = new UserService(dataAccess.user(), dataAccess.auth());
        jsonService = new JsonService();
        gamesService = new GamesService();
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
}
