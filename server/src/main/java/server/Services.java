package server;

import service.ClearService;
import service.UserService;

public class Services {
    private final ClearService clearService;
    private final UserService userService;

    public Services(DataAccess dataAccess) {
        clearService = new ClearService(dataAccess.auth(), dataAccess.user(), dataAccess.games());
        userService = new UserService(dataAccess.user(), dataAccess.auth());
    }

    public ClearService clearService() {
        return clearService;
    }

    public UserService userService() {
        return userService;
    }
}
