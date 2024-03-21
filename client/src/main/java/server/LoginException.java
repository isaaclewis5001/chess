package server;

public class LoginException extends Exception {
    public LoginException() {
        super("Bad login credentials");
    }
}
