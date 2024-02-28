package serviceTests;

import model.AuthData;
import model.UserData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.bundles.DataAccess;
import server.bundles.MemoryDataAccess;
import server.bundles.Services;
import service.GamesService;
import service.UserService;


@DisplayName("Clear Service Tests")
public class ClearServiceTests {
    private Services services;

    private void setup() {
        DataAccess dataAccess = new MemoryDataAccess();
        services = new Services(dataAccess);
    }

    @Test
    @DisplayName("Clear")
    public void clear() throws Exception {
        setup();

        AuthData auth = services.userService().createUser(new UserData("a", "b", "c"));
        int game = services.gamesService().createGame(new CreateGameRequest("game")).gameID();

        services.clearService().clear();

        Assertions.assertThrows(UserService.BadLoginException.class,
            () -> services.userService().login(new LoginRequest("a", "b"))
        );

        Assertions.assertThrows(UserService.BadAuthException.class,
            () -> services.userService().getAuthUser(auth.authToken())
        );

        Assertions.assertThrows(GamesService.BadGameIdException.class,
            () -> services.gamesService().joinGame(new JoinGameRequest(game, null), "a")
        );
    }
}
