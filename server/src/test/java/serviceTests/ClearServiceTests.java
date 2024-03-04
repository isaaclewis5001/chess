package serviceTests;

import model.AuthData;
import model.UserData;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataAccess.bundles.DataAccessBundle;
import dataAccess.bundles.MemoryDataAccessBundle;
import service.ServiceBundle;
import service.GamesService;
import service.UserService;


@DisplayName("Clear Service Tests")
public class ClearServiceTests {
    private ServiceBundle serviceBundle;

    private void setup() {
        DataAccessBundle dataAccessBundle = new MemoryDataAccessBundle();
        serviceBundle = new ServiceBundle(dataAccessBundle);
    }

    @Test
    @DisplayName("Clear")
    public void clear() throws Exception {
        setup();

        AuthData auth = serviceBundle.userService().createUser(new UserData("a", "b", "c"));
        int game = serviceBundle.gamesService().createGame(new CreateGameRequest("game")).gameID();

        serviceBundle.clearService().clear();

        Assertions.assertThrows(UserService.BadLoginException.class,
            () -> serviceBundle.userService().login(new LoginRequest("a", "b"))
        );

        Assertions.assertThrows(UserService.BadAuthException.class,
            () -> serviceBundle.userService().getAuthUser(auth.authToken())
        );

        Assertions.assertThrows(GamesService.BadGameIdException.class,
            () -> serviceBundle.gamesService().joinGame(new JoinGameRequest(game, null), "a")
        );
    }
}
