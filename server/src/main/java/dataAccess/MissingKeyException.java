package dataAccess;

public class MissingKeyException extends DataAccessException {
    public MissingKeyException(String keyName, String value) {
        super(keyName + " = " + value + " not found");
    }
}
