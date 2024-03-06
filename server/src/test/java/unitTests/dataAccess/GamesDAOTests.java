package unitTests.dataAccess;

import dataAccess.DatabaseException;
import dataAccess.games.GamesDAO;
import dataAccess.games.MemoryGamesDAO;
import dataAccess.games.SQLGamesDAO;
import org.junit.jupiter.api.DisplayName;


@DisplayName("Games DAO Tests")
public class GamesDAOTests {
    static GamesDAO[] getImplementors() throws DatabaseException {
        return new GamesDAO[] {
                new MemoryGamesDAO(),
                new SQLGamesDAO(),
        };
    }
}
