package server;

public class TeamTakenException extends Exception {
    public TeamTakenException() {
        super("The requested team was already taken.");
    }
}
