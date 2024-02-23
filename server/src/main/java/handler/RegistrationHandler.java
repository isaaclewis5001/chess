package handler;

import model.AuthData;
import model.UserData;
import model.response.ErrorResponse;
import service.JsonService;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegistrationHandler {
    private final UserService userService;
    private final JsonService jsonService;


    public Object createUser(Request request, Response response) {
        UserData userData;
        try {
            userData = jsonService.validFromJson(request.body(), UserData.class);
        } catch(Exception ex) {
            response.status(400);
            return jsonService.toJson(new ErrorResponse("bad request"));
        }

        AuthData authData;
        try {
            authData = userService.createUser(userData);
        }
        catch (UserService.UsernameTakenException ex) {
            response.status(403);
            return jsonService.toJson(new ErrorResponse("username already taken"));
        }

        return jsonService.toJson(authData);
    }

    public RegistrationHandler(UserService userService, JsonService jsonService) {
        this.userService = userService;
        this.jsonService = jsonService;
    }
}
