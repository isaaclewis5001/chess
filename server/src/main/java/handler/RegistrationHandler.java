package handler;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import model.response.ErrorResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegistrationHandler {
    private final UserService userService;
    private final Gson gson;

    private UserData parseUserData(String json) {
        UserData userData = gson.fromJson(json, UserData.class);
        if (!userData.validateFields()) {
            throw new RuntimeException("Found user data with missing or invalid fields");
        }
        return userData;
    }

    public Object createUser(Request request, Response response) {
        UserData userData;
        try {
            userData = parseUserData(request.body());
        } catch(Exception ex) {
            response.status(400);
            return gson.toJson(new ErrorResponse("bad request"));
        }

        AuthData authData;
        try {
            authData = userService.createUser(userData);
        }
        catch (UserService.UsernameTakenException ex) {
            response.status(403);
            return gson.toJson(new ErrorResponse("username already taken"));
        }

        return gson.toJson(authData);
    }

    public RegistrationHandler(UserService userService) {
        this.userService = userService;
        gson = new Gson();
    }
}
