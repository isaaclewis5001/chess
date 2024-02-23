package model.validation;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super("Invalid value: " + message);
    }
}
