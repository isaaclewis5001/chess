package model.request;

import model.validation.Validate;
import model.validation.ValidationException;

public record CreateGameRequest(String gameName) implements Validate {
    @Override
    public void validate() throws ValidationException {
        if (gameName == null || gameName.isEmpty()) {
            throw new ValidationException("Game name cannot be null or empty");
        }
    }
}
