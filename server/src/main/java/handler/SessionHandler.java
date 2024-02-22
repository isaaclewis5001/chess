package handler;

import com.google.gson.Gson;
import service.UserService;

public class SessionHandler {
    private final UserService userService;
    private final Gson gson;

    public SessionHandler(UserService userService) {
        this.userService = userService;
        gson = new Gson();
    }
}
