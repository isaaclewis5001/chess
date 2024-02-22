package dataAccess;

public class BadUpdateException extends DataAccessException {
    public BadUpdateException(String reason) {
        super("Update operation failed:" + reason);
    }
}
