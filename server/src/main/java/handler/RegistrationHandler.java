package handler;

import dataAccess.DatabaseException;
import model.AuthData;
import model.request.CreateUserRequest;
import model.response.ErrorResponse;
import service.JsonService;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegistrationHandler {
    private final UserService userService;
    private final JsonService jsonService;


    public Object createUser(Request request, Response response) {
        CreateUserRequest createUserRequest;
        try {
            createUserRequest = jsonService.validFromJson(request.body(), CreateUserRequest.class);
        } catch(Exception ex) {
            response.status(400);
            return jsonService.toJson(new ErrorResponse("bad request"));
        }

        AuthData authData;
        try {
            authData = userService.createUser(createUserRequest);
        }
        catch (UserService.UsernameTakenException ex) {
            response.status(403);
            return jsonService.toJson(new ErrorResponse("username already taken"));
        }
        catch (DatabaseException ex) {
            response.status(500);
            return jsonService.toJson(new ErrorResponse("internal error"));
        }

        return jsonService.toJson(authData);
    }

    public RegistrationHandler(UserService userService, JsonService jsonService) {
        this.userService = userService;
        this.jsonService = jsonService;
    }
}
