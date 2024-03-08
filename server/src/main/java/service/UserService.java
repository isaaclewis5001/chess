package service;

import dataAccess.DatabaseException;
import dataAccess.DuplicateKeyException;
import dataAccess.MissingKeyException;
import dataAccess.auth.AuthDAO;
import dataAccess.user.UserDAO;
import model.AuthData;
import model.UserData;
import model.request.CreateUserRequest;
import model.request.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    private final BCryptPasswordEncoder passwordEncoder;

    public static class UsernameTakenException extends Exception {

        public UsernameTakenException(Throwable cause) {
            super("Username already taken", cause);
        }
    }

    public static class BadLoginException extends Exception {
        public BadLoginException() {
            this(null);
        }
        public BadLoginException(Throwable cause) {
            super("Bad login credentials", cause);
        }
    }

    public static class BadAuthException extends Exception {
        public BadAuthException() {
            super("Bad auth token");
        }
    }

    public AuthData createUser(CreateUserRequest createUserRequest) throws UsernameTakenException, DatabaseException {
        UserData userData = new UserData(createUserRequest, passwordEncoder);
        try {
            userDAO.createUser(userData);
        } catch (DuplicateKeyException ex) {
            throw new UsernameTakenException(ex);
        }
        return createAuthForUser(userData.username());
    }

    public AuthData login(LoginRequest request) throws BadLoginException, DatabaseException {
        UserData user;
        try {
            user = userDAO.getUserByUsername(request.username());
        } catch (MissingKeyException ex) {
            throw new BadLoginException(ex);
        }

        if (!user.passwordMatches(request.password(), passwordEncoder)) {
            throw new BadLoginException();
        }
         return createAuthForUser(request.username());
    }

    public void logout(String authToken) throws BadAuthException, DatabaseException {
        if (authToken == null) {
            throw new BadAuthException();
        }
        try {
            authDAO.removeAuth(authToken);
        } catch (MissingKeyException ex) {
            throw new BadAuthException();
        }
    }


    private AuthData createAuthForUser(String username) throws DatabaseException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        try {
            authDAO.addAuth(authData);
        } catch (DuplicateKeyException ex) {
            Logger.getGlobal().log(Level.WARNING, "AuthToken generated for user " + username + " was not unique");
            throw new RuntimeException("Failed to create a unique UUID as auth token.", ex);
        }
        return authData;
    }

    public AuthData getAuthUser(String authToken) throws BadAuthException, DatabaseException {
        try {
            return authDAO.getAuthUser(authToken);
        } catch(MissingKeyException ex) {
            throw new BadAuthException();
        }
    }

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
}
