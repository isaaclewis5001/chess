package handler;

import model.AuthData;
import model.request.LoginRequest;
import model.response.ErrorResponse;
import service.JsonService;
import service.UserService;
import spark.Request;
import spark.Response;

public class SessionHandler {
    private final UserService userService;
    private final JsonService jsonService;

    public Object login(Request request, Response response) {
        LoginRequest loginRequest;
        try {
            loginRequest = jsonService.validFromJson(request.body(), LoginRequest.class);
        } catch (Exception ex) {
            response.status(400);
            return jsonService.toJson(new ErrorResponse("bad request"));
        }

        AuthData authData;
        try {
            authData = userService.login(loginRequest);
        } catch (UserService.BadLoginException ex) {
            response.status(401);
            return jsonService.toJson(new ErrorResponse("unauthorized"));
        }

        return jsonService.toJson(authData);
    }

    public Object logout(Request request, Response response) {
        String authToken = request.headers("authorization");

        try {
            userService.logout(authToken);
        } catch (Exception ex) {
            response.status(401);
            return jsonService.toJson(new ErrorResponse("unauthorized"));
        }

        return "";
    }

    public SessionHandler(UserService userService, JsonService jsonService) {
        this.userService = userService;
        this.jsonService = jsonService;
    }
}
