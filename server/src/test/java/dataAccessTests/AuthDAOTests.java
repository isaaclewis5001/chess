package dataAccessTests;

import dataAccess.DatabaseException;
import dataAccess.DatabaseManager;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import dataAccess.auth.AuthDAO;
import dataAccess.auth.MemoryAuthDAO;
import dataAccess.auth.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Auth DAO Tests")
public class AuthDAOTests {
    static AuthDAO[] getImplementors() throws DatabaseException {
        DatabaseManager.createDatabase();
        return new AuthDAO[] {
            new MemoryAuthDAO(),
            new SQLAuthDAO(),
        };
    }

    @Test
    @DisplayName("Recall")
    void recallAuth() throws Exception {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("2", "betty");
        AuthData auth3 = new AuthData("3", "cay");
        for (AuthDAO impl: getImplementors()) {
            impl.clear();
            impl.addAuth(auth1);
            impl.addAuth(auth2);

            Assertions.assertEquals(impl.getAuthUser("1"), auth1);
            Assertions.assertEquals(impl.getAuthUser("2"), auth2);

            impl.addAuth(auth3);

            Assertions.assertEquals(impl.getAuthUser("1"), auth1);
            Assertions.assertEquals(impl.getAuthUser("3"), auth3);

            Assertions.assertThrows(MissingKeyException.class, () -> impl.getAuthUser("4"));
        }
    }

    @Test
    @DisplayName("Clear")
    void clear() throws Exception {
        AuthData auth1 = new AuthData("1", "alex");
        for (AuthDAO impl: getImplementors()) {
            impl.clear();
            impl.addAuth(auth1);
            impl.clear();
            Assertions.assertThrows(MissingKeyException.class, () -> impl.getAuthUser("1"));
        }
    }

    @Test
    @DisplayName("Remove")
    void remove() throws Exception {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("2", "betty");
        for (AuthDAO impl: getImplementors()) {
            impl.clear();
            impl.addAuth(auth1);
            impl.addAuth(auth2);
            impl.removeAuth("1");
            Assertions.assertThrows(MissingKeyException.class, () -> impl.getAuthUser("1"));
            Assertions.assertNotNull(impl.getAuthUser("2"));
        }
    }

    @Test
    @DisplayName("Handle Duplicate Tokens")
    void handeDuplicates() throws Exception {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("1", "dwight");
        for (AuthDAO impl: getImplementors()) {
            impl.clear();
            impl.addAuth(auth1);
            Assertions.assertThrows(DuplicateKeyException.class, () -> impl.addAuth(auth2));
        }
    }
}
