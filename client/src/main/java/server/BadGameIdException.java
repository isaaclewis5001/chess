package server;

public class BadGameIdException extends Exception {
    public BadGameIdException() {
        super("Bad game ID");
    }
}
