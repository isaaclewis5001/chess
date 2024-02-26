package unitTests.dataAccess;

import chess.ChessGame;
import dataAccess.BadUpdateException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import dataAccess.games.GamesDAO;
import dataAccess.games.MemoryGamesDAO;
import model.GameData;
import model.GameDesc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;


@DisplayName("Games DAO Tests")
public class GamesDAOTests {
    static GamesDAO[] getImplementors() {
        return new GamesDAO[] {
                new MemoryGamesDAO()
        };
    }

    @Test
    @DisplayName("Recall")
    void recallGames() throws Exception {
        GameData game1 = new GameData(1, "alex", "betty", "ab", null);
        GameData game2 = new GameData(2, "betty", "cay", "bc", null);
        GameData game3 = new GameData(3, "alex", "cay", "ac", null);
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            impl.createGame(game2);

            Assertions.assertEquals(game1, impl.fetchGame(1));
            Assertions.assertEquals(game2, impl.fetchGame(2));

            impl.createGame(game3);

            Assertions.assertEquals(game1, impl.fetchGame(1));
            Assertions.assertEquals(game3, impl.fetchGame(3));

            Assertions.assertThrows(MissingKeyException.class, () -> impl.fetchGame(4));
        }
    }

    @Test
    @DisplayName("List")
    void listGames() throws Exception {
        GameData game1 = new GameData(1, "alex", "betty", "ab", null);
        GameData game2 = new GameData(2, "betty", "cay", "bc", new ChessGame());
        GameData game3 = new GameData(3, "alex", "cay", "ac", null);

        HashSet<GameDesc> expectedSet = new HashSet<>();
        expectedSet.add(game1.desc());
        expectedSet.add(game2.desc());
        expectedSet.add(game3.desc());

        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            impl.createGame(game2);
            impl.createGame(game3);

            Assertions.assertEquals(expectedSet, new HashSet<>(impl.listGames()));
        }
    }

    @Test
    @DisplayName("Clear")
    void clear() throws Exception {
        GameData game1 = new GameData(1, "alex", "betty", "ab", null);
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            impl.clear();
            Assertions.assertThrows(MissingKeyException.class, () -> impl.fetchGame(1));
            Assertions.assertTrue(impl.listGames().isEmpty());
        }
    }

    @Test
    @DisplayName("Remove")
    void remove() throws Exception {
        GameData game1 = new GameData(1, "alex", "betty", "ab", null);
        GameData game2 = new GameData(2, "betty", "cay", "bc", null);

        HashSet<GameDesc> expectedSet = new HashSet<>();
        expectedSet.add(game2.desc());
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            impl.createGame(game2);
            impl.removeGame(1);

            Assertions.assertThrows(MissingKeyException.class, () -> impl.fetchGame(1));
            Assertions.assertEquals(game2, impl.fetchGame(2));

            Assertions.assertEquals(expectedSet, new HashSet<>(impl.listGames()));
        }
    }

    @Test
    @DisplayName("Join Game")
    void joinGame() throws Exception {
        GameData game1 = new GameData(1, null, null, "ab", null);
        GameData game1Result = new GameData(1, "alex", "betty", "ab", null);
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            impl.updateGameParticipants(1, ChessGame.TeamColor.BLACK, "betty");
            impl.updateGameParticipants(1, ChessGame.TeamColor.WHITE, "alex");
            Assertions.assertEquals(game1Result, impl.fetchGame(1));
        }
    }

    @Test
    @DisplayName("Team Already Taken")
    void teamTaken() throws Exception {
        GameData game1 = new GameData(1, null, null, "jumanji", null);
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);

            impl.updateGameParticipants(1, ChessGame.TeamColor.WHITE, "alex");
            Assertions.assertThrows(BadUpdateException.class,
                () -> impl.updateGameParticipants(1, ChessGame.TeamColor.WHITE, "dwight")
            );

            impl.updateGameParticipants(1, ChessGame.TeamColor.BLACK, "betty");
            Assertions.assertThrows(BadUpdateException.class,
                    () -> impl.updateGameParticipants(1, ChessGame.TeamColor.BLACK, "dwight")
            );
        }
    }

    @Test
    @DisplayName("Handle Duplicate IDs")
    void handeDuplicates() throws Exception {
        GameData game1 = new GameData(1, "alex", "betty", "ab", null);
        GameData game2 = new GameData(1, "dwight", "dwight", "dd", null);
        for (GamesDAO impl: getImplementors()) {
            impl.createGame(game1);
            Assertions.assertThrows(DuplicateKeyException.class, () -> impl.createGame(game2));
        }
    }
}
