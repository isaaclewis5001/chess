package serviceTests;


import dataAccess.auth.MemoryAuthDAO;
import dataAccess.user.MemoryUserDAO;
import model.data.AuthData;
import model.request.CreateUserRequest;
import model.request.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;


@DisplayName("User Service Tests")
public class UserServiceTests {
    private static final CreateUserRequest user1 = new CreateUserRequest("shauna", "ice_cream_with_jimmies2", "shauna@byu.edu");
    private static final CreateUserRequest user1Alt = new CreateUserRequest("shauna", "jimmy-less", "shauna@irs.gov");
    private static final CreateUserRequest user2 = new CreateUserRequest("bob", "just_bobbin_along", "bob@byu.edu");
    private UserService createService() {
        return new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    }

    @Test
    @DisplayName("Create Users")
    public void createUsers() throws Exception {
        UserService service = createService();

        AuthData auth1 = service.createUser(user1);
        AuthData auth2 = service.createUser(user2);

        Assertions.assertNotEquals(auth1.authToken(), auth2.authToken());
        Assertions.assertEquals(user1.username(), auth1.username());
        Assertions.assertEquals(user2.username(), auth2.username());
    }

    @Test
    @DisplayName("Duplicate Users")
    public void duplicateUsers() throws Exception {
        UserService service = createService();

        service.createUser(user1);
        service.createUser(user2);

        Assertions.assertThrows(UserService.UsernameTakenException.class,
            () -> service.createUser(user1Alt)
        );
    }

    @Test
    @DisplayName("Get User")
    public void getUser() throws Exception {
        UserService service = createService();

        AuthData auth = service.createUser(user1);

        Assertions.assertEquals(user1.username(), service.getAuthUser(auth.authToken()).username());
    }

    @Test
    @DisplayName("Get User with Bad Auth")
    public void getUserWithBadAuth() throws Exception {
        UserService service = createService();

        service.createUser(user1);

        Assertions.assertThrows(UserService.BadAuthException.class, () -> service.getAuthUser("Pres. Reese's secret auth token"));
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws Exception {
        UserService service = createService();

        AuthData auth = service.createUser(user1);
        service.logout(auth.authToken());

        Assertions.assertThrows(UserService.BadAuthException.class, () -> service.getAuthUser(auth.authToken()));
    }

    @Test
    @DisplayName("Logout Twice")
    public void logoutTwice() throws Exception {
        UserService service = createService();

        AuthData auth = service.createUser(user1);
        service.logout(auth.authToken());

        Assertions.assertThrows(UserService.BadAuthException.class, () -> service.logout(auth.authToken()));
    }

    @Test
    @DisplayName("Login")
    public void login() throws Exception {
        UserService service = createService();

        AuthData auth = service.createUser(user1);
        service.logout(auth.authToken());

        AuthData newAuth = service.login(new LoginRequest(user1.username(), user1.password()));

        Assertions.assertEquals(user1.username(), service.getAuthUser(newAuth.authToken()).username());
    }

    @Test
    @DisplayName("Bad Login")
    public void badLogin() throws Exception {
        UserService service = createService();

        AuthData auth = service.createUser(user1);
        service.logout(auth.authToken());

        Assertions.assertThrows(UserService.BadLoginException.class,
            () -> service.login(new LoginRequest(user1.username(), "fish_in_the_pool"))
        );
    }
}
