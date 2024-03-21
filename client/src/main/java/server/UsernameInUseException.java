package server;

public class UsernameInUseException extends Exception {
    public UsernameInUseException() {
        super("Username taken.");
    }
}
