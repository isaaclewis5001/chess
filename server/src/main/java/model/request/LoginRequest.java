package model.request;

import model.validation.Validate;
import model.validation.ValidationException;

public record LoginRequest(String username, String password) implements Validate {
    @Override
    public void validate() throws ValidationException {
        if (username == null || password == null) {
            throw new ValidationException("Field was missing");
        }

        if (username.isEmpty() || password.isEmpty()) {
            throw new ValidationException("Field was empty");
        }
    }
}
