package model;

import model.validation.Validate;
import model.validation.ValidationException;

public record UserData(String username, String password, String email) implements Validate {
    @Override
    public void validate() throws ValidationException {
        if (
            username == null ||
            password == null ||
            email == null
        ) {
            throw new ValidationException("Field was missing");
        }

        if (
            username.isEmpty() ||
            password.isEmpty() ||
            email.isEmpty()
        ) {
            throw new ValidationException("Field was empty");
        }
    }
}
