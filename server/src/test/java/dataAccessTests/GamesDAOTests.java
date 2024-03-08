package dataAccessTests;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DatabaseException;
import dataAccess.DatabaseManager;
import dataAccess.MissingKeyException;
import dataAccess.games.GamesDAO;
import dataAccess.games.MemoryGamesDAO;
import dataAccess.games.SQLGamesDAO;
import model.GameDesc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;


@DisplayName("Games DAO Tests")
public class GamesDAOTests {
    static GamesDAO[] getImplementors() throws DatabaseException {
        DatabaseManager.createDatabase();
        return new GamesDAO[] {
                new MemoryGamesDAO(),
                new SQLGamesDAO(),
        };
    }


    @DisplayName("Distinct IDs")
    @Test
    public void distinctIds() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");
            int id2 = impl.createGame("Game 2");

            Assertions.assertNotEquals(id1, id2);
        }
    }

    @DisplayName("List Games")
    @Test
    public void listGames() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");
            int id2 = impl.createGame("Game 2");

            HashSet<GameDesc> actualSet = new HashSet<>(impl.listGames());
            HashSet<GameDesc> expectedSet = new HashSet<>();

            expectedSet.add(new GameDesc(id1, null, null, "Game 1"));
            expectedSet.add(new GameDesc(id2, null, null, "Game 2"));

            Assertions.assertEquals(expectedSet, actualSet);
        }
    }

    @DisplayName("Join Game")
    @Test
    public void joinGame() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");
            int id2 = impl.createGame("Game 2");
            int id3 = impl.createGame("Game 3");
            int id4 = impl.createGame("Game 4");

            impl.updateGameParticipants(id2, ChessGame.TeamColor.WHITE, "w");
            impl.updateGameParticipants(id4, ChessGame.TeamColor.WHITE, "w");
            impl.updateGameParticipants(id3, ChessGame.TeamColor.BLACK, "b");
            impl.updateGameParticipants(id4, ChessGame.TeamColor.BLACK, "b");

            HashSet<GameDesc> actualSet = new HashSet<>(impl.listGames());
            HashSet<GameDesc> expectedSet = new HashSet<>();

            expectedSet.add(new GameDesc(id1, null, null, "Game 1"));
            expectedSet.add(new GameDesc(id2, "w", null, "Game 2"));
            expectedSet.add(new GameDesc(id3, null, "b", "Game 3"));
            expectedSet.add(new GameDesc(id4, "w", "b", "Game 4"));

            Assertions.assertEquals(expectedSet, actualSet);
        }
    }

    @DisplayName("Join Game Bad ID")
    @Test
    public void joinGameBadId() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");

            Assertions.assertThrows(MissingKeyException.class,
                () -> impl.updateGameParticipants(id1 + 1, ChessGame.TeamColor.BLACK, "forrest")
            );
        }
    }

    @DisplayName("Team Taken")
    @Test
    public void teamTaken() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");

            impl.updateGameParticipants(id1, ChessGame.TeamColor.WHITE, "w");
            impl.updateGameParticipants(id1, ChessGame.TeamColor.BLACK, "b");

            Assertions.assertThrows(BadUpdateException.class,
                () -> impl.updateGameParticipants(id1, ChessGame.TeamColor.WHITE, "w2")
            );

            Assertions.assertThrows(BadUpdateException.class,
                () -> impl.updateGameParticipants(id1, ChessGame.TeamColor.BLACK, "b2")
            );
        }
    }

    @DisplayName("Game Exists")
    @Test
    public void gameExists() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");

            Assertions.assertTrue(impl.gameExists(id1));
            Assertions.assertFalse(impl.gameExists(id1 + 1));
        }
    }

    @DisplayName("Clear")
    @Test
    public void clear() throws Exception {
        for (GamesDAO impl: getImplementors()) {
            impl.clear();
            int id1 = impl.createGame("Game 1");

            impl.clear();
            Assertions.assertTrue(impl.listGames().isEmpty());
            Assertions.assertFalse(impl.gameExists(id1));
        }
    }
}
