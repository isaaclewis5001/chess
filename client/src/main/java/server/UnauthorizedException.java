package server;

public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super("Bad authorization.");
    }
}
