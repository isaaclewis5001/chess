package dataAccess;

public class DuplicateKeyException extends DataAccessException {
    public DuplicateKeyException(String keyName, String value) {
        super("Found duplicate " + keyName + " with value " + value);
    }
}
