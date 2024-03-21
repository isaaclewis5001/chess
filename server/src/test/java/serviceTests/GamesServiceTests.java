package serviceTests;

import chess.ChessGame;
import dataAccess.games.MemoryGamesDAO;
import model.data.GameDesc;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GamesService;

import java.util.HashSet;

@DisplayName("Games Service Tests")
public class GamesServiceTests {
    private GamesService service;
    private void createService() {
        service = new GamesService(new MemoryGamesDAO());
    }

    private int createGame(String name) throws Exception {
        return service.createGame(new CreateGameRequest(name)).gameID();
    }

    private void join(int gameId, ChessGame.TeamColor color, String username) throws Exception {
        service.joinGame(new JoinGameRequest(gameId, color), username);
    }
    @Test
    @DisplayName("List Games")
    public void listGames() throws Exception {
        createService();

        int game1 = createGame("my game");
        int game2 = createGame("another game");

        Assertions.assertNotEquals(game1, game2);

        HashSet<GameDesc> games = new HashSet<>(service.listGames());

        HashSet<GameDesc> expected = new HashSet<>();
        expected.add(new GameDesc(game1, null, null, "my game"));
        expected.add(new GameDesc(game2, null, null, "another game"));

        Assertions.assertEquals(expected, games);
    }

    @Test
    @DisplayName("Join Game")
    public void joinGame() throws Exception {
        createService();

        int game1 = createGame("none");
        int game2 = createGame("white");
        int game3 = createGame("black");
        int game4 = createGame("both");

        join(game2, ChessGame.TeamColor.WHITE, "white");
        join(game4, ChessGame.TeamColor.WHITE, "white");

        join(game3, ChessGame.TeamColor.BLACK, "black");
        join(game4, ChessGame.TeamColor.BLACK, "black");

        HashSet<GameDesc> expectedGames = new HashSet<>();
        expectedGames.add(new GameDesc(game1, null, null, "none"));
        expectedGames.add(new GameDesc(game2, "white", null, "white"));
        expectedGames.add(new GameDesc(game3, null, "black", "black"));
        expectedGames.add(new GameDesc(game4, "white", "black", "both"));

        HashSet<GameDesc> actualGames = new HashSet<>(service.listGames());

        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    @DisplayName("Team Taken")
    public void teamTaken() throws Exception {
        createService();

        int game = createGame("game");

        join(game, ChessGame.TeamColor.WHITE, "white");
        join(game, ChessGame.TeamColor.BLACK, "black");

        Assertions.assertThrows(GamesService.TeamTakenException.class,
            () -> join(game, ChessGame.TeamColor.WHITE, "other")
        );

        Assertions.assertThrows(GamesService.TeamTakenException.class,
                () -> join(game, ChessGame.TeamColor.BLACK, "other")
        );

        HashSet<GameDesc> expectedGames = new HashSet<>();
        expectedGames.add(new GameDesc(game, "white", "black", "game"));

        HashSet<GameDesc> actualGames = new HashSet<>(service.listGames());

        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    @DisplayName("Watch Game")
    public void watchGame() throws Exception {
        createService();

        int game = createGame("game");

        join(game, ChessGame.TeamColor.WHITE, "white");
        join(game, ChessGame.TeamColor.BLACK, "black");

        join(game, null, "other");

        HashSet<GameDesc> expectedGames = new HashSet<>();
        expectedGames.add(new GameDesc(game, "white", "black", "game"));

        HashSet<GameDesc> actualGames = new HashSet<>(service.listGames());

        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    @DisplayName("Join Bad Game")
    public void joinBadGame() throws Exception {
        createService();

        int game = createGame("game");

        Assertions.assertThrows(GamesService.BadGameIdException.class,
                () -> join(~game, ChessGame.TeamColor.WHITE, "some poor soul")
        );
        Assertions.assertThrows(GamesService.BadGameIdException.class,
                () -> join(~game, ChessGame.TeamColor.BLACK, "some poor soul")
        );
    }
    @Test
    @DisplayName("Watch Bad Game")
    public void watchBadGame() throws Exception {
        createService();

        int game = createGame("game");

        Assertions.assertThrows(GamesService.BadGameIdException.class,
            () -> join(~game, null, "some poor soul")
        );
    }
}
