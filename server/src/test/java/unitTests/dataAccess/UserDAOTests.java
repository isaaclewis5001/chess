package unitTests.dataAccess;

import dataAccess.user.MemoryUserDAO;
import dataAccess.user.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("User DAO Tests")
public class UserDAOTests {
    static UserDAO[] getImplementors() {
        return new UserDAO[] {
                new MemoryUserDAO()
        };
    }

    @Test
    @DisplayName("Recall Users")
    void recallUsers() {
        UserData user1 = new UserData("alex", "pancakes14", "alex@mail.au");
        UserData user2 = new UserData("betty", "blu3b3rri3s", "betty@hotmail.com");
        UserData user3 = new UserData("cay", "gray_skull2", "the_he_man@gmail.com");
        for (UserDAO impl: getImplementors()) {
            impl.createUser(user1);
            impl.createUser(user2);

            Assertions.assertEquals(impl.getUserByUsername("alex"), user1);
            Assertions.assertEquals(impl.getUserByUsername("betty"), user2);

            impl.createUser(user3);

            Assertions.assertEquals(impl.getUserByUsername("alex"), user1);
            Assertions.assertEquals(impl.getUserByUsername("cay"), user3);
        }
    }

    @Test
    @DisplayName("Clear")
    void clear() {
        UserData user1 = new UserData("alex", "pancakes14", "alex@mail.au");
        for (UserDAO impl: getImplementors()) {
            impl.createUser(user1);
            impl.clear();
            Assertions.assertNull(impl.getUserByUsername("alex"));
        }
    }

    @Test
    @DisplayName("Handle Duplicate Usernames")
    void handeDuplicates() {
        UserData user1 = new UserData("alex", "pancakes14", "alex@mail.au");
        UserData user2 = new UserData("alex", "pancakes15", "fakealex@email.gov");
        for (UserDAO impl: getImplementors()) {
            impl.createUser(user1);
            Assertions.assertThrows(RuntimeException.class, () -> impl.createUser(user2));
        }
    }
}
