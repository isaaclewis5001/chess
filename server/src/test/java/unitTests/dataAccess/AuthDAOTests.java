package unitTests.dataAccess;

import dataAccess.auth.AuthDAO;
import dataAccess.auth.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Auth DAO Tests")
public class AuthDAOTests {
    static AuthDAO[] getImplementors() {
        return new AuthDAO[] {
                new MemoryAuthDAO()
        };
    }

    @Test
    @DisplayName("Recall")
    void recallAuth() {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("2", "betty");
        AuthData auth3 = new AuthData("3", "cay");
        for (AuthDAO impl: getImplementors()) {
            impl.addAuth(auth1);
            impl.addAuth(auth2);

            Assertions.assertEquals(impl.getAuthUser("1"), auth1);
            Assertions.assertEquals(impl.getAuthUser("2"), auth2);

            impl.addAuth(auth3);

            Assertions.assertEquals(impl.getAuthUser("1"), auth1);
            Assertions.assertEquals(impl.getAuthUser("3"), auth3);

            Assertions.assertNull(impl.getAuthUser("4"));
        }
    }

    @Test
    @DisplayName("Clear")
    void clear() {
        AuthData auth1 = new AuthData("1", "alex");
        for (AuthDAO impl: getImplementors()) {
            impl.addAuth(auth1);
            impl.clear();
            Assertions.assertNull(impl.getAuthUser("alex"));
        }
    }

    @Test
    @DisplayName("Remove")
    void remove() {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("2", "betty");
        for (AuthDAO impl: getImplementors()) {
            impl.addAuth(auth1);
            impl.addAuth(auth2);
            impl.removeAuth("1");
            Assertions.assertNull(impl.getAuthUser("1"));
            Assertions.assertNotNull(impl.getAuthUser("2"));
        }
    }

    @Test
    @DisplayName("Handle Duplicate Tokens")
    void handeDuplicates() {
        AuthData auth1 = new AuthData("1", "alex");
        AuthData auth2 = new AuthData("1", "dwight");
        for (AuthDAO impl: getImplementors()) {
            impl.addAuth(auth1);
            Assertions.assertThrows(RuntimeException.class, () -> impl.addAuth(auth2));
        }
    }
}
