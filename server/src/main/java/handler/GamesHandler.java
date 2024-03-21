package handler;

import dataAccess.DatabaseException;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.response.ErrorResponse;
import model.response.ListGamesResponse;
import service.GamesService;
import service.JsonService;
import service.UserService;
import spark.Request;
import spark.Response;

public class GamesHandler {
    private final UserService userService;
    private final JsonService jsonService;
    private final GamesService gamesService;


    public Object listGames(Request request, Response response) {
        try {
            String auth = request.headers("authorization");
            try {
                userService.getAuthUser(auth);
            } catch (UserService.BadAuthException ex) {
                response.status(401);
                return jsonService.toJson(new ErrorResponse("unauthorized"));
            }

            ListGamesResponse listGamesResponse = new ListGamesResponse(gamesService.listGames());

            return jsonService.toJson(listGamesResponse);
        } catch (DatabaseException ex) {
            response.status(500);
            return jsonService.toJson(new ErrorResponse("internal error"));
        }
    }


    public Object createGame(Request request, Response response) {
        try {
            String auth = request.headers("authorization");
            try {
                userService.getAuthUser(auth);
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
        } catch (DatabaseException ex) {
            response.status(500);
            return jsonService.toJson(new ErrorResponse("internal error"));
        }
    }

    public Object joinGame(Request request, Response response) {
        try {
            String auth = request.headers("authorization");
            String username;
            try {
                username = userService.getAuthUser(auth).username();
            } catch (UserService.BadAuthException ex) {
                response.status(401);
                return jsonService.toJson(new ErrorResponse("unauthorized"));
            }

            JoinGameRequest joinGameRequest;
            try {
                joinGameRequest = jsonService.fromJson(request.body(), JoinGameRequest.class);
            } catch (JsonService.JsonException ex) {
                response.status(400);
                return jsonService.toJson(new ErrorResponse("bad request"));
            }

            try {
                gamesService.joinGame(joinGameRequest, username);
            } catch (GamesService.TeamTakenException ex) {
                response.status(403);
                return jsonService.toJson(new ErrorResponse("already taken"));
            } catch (GamesService.BadGameIdException ex) {
                response.status(400);
                return jsonService.toJson(new ErrorResponse("bad game id"));
            }

            return "";
        }
        catch (DatabaseException ex) {
            response.status(500);
            return jsonService.toJson(new ErrorResponse("internal error"));
        }
    }

    public GamesHandler(UserService userService, JsonService jsonService, GamesService gamesService) {
        this.userService = userService;
        this.jsonService = jsonService;
        this.gamesService = gamesService;
    }
}
