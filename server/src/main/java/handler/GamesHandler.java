package handler;

import model.AuthData;
import model.request.CreateGameRequest;
import model.response.CreateGameResponse;
import model.response.ErrorResponse;
import model.response.ListGamesResponse;
import service.GamesService;
import service.JsonService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class GamesHandler {
    private final UserService userService;
    private final JsonService jsonService;
    private final GamesService gamesService;


    public Object listGames(Request request, Response response) {
        String auth = request.headers("authorization");
        try {
            userService.getAuthUser(auth);
        } catch (UserService.BadAuthException ex) {
            response.status(401);
            return jsonService.toJson(new ErrorResponse("unauthorized"));
        }

        ListGamesResponse listGamesResponse = new ListGamesResponse(gamesService.listGames());

        return jsonService.toJson(listGamesResponse);
    }


    public Object createGame(Request request, Response response) {
        String auth = request.headers("authorization");
        try {
            userService.getAuthUser(auth).username();
        } catch (UserService.BadAuthException ex) {
            response.status(401);
            return jsonService.toJson(new ErrorResponse("unauthorized"));
        }

        CreateGameRequest requestBody;

        try {
            requestBody = jsonService.validFromJson(request.body(), CreateGameRequest.class);
        } catch (Exception ex) {
            response.status(400);
            return jsonService.toJson(new ErrorResponse("bad request"));
        }

        return jsonService.toJson(gamesService.createGame(requestBody));
    }

    public GamesHandler(UserService userService, JsonService jsonService, GamesService gamesService) {
        this.userService = userService;
        this.jsonService = jsonService;
        this.gamesService = gamesService;
    }
}
