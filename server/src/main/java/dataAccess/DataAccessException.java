package dataAccess;

/**
 * Indicates there was an error during a data access operation
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }
}
