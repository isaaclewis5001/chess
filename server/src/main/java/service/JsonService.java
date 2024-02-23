package service;

import com.google.gson.Gson;
import model.validation.Validate;
import model.validation.ValidationException;

public class JsonService {
    private final Gson gson;

    public static class JsonException extends Exception {
        public JsonException(Exception cause) {
            super("Failed to parse JSON", cause);
        }
    }


    public String toJson(Object object) {
        return gson.toJson(object);
    }

    public <T> T fromJson(String input, Class<T> tClass) throws JsonException {
        try {
            return gson.fromJson(input, tClass);
        } catch(Exception ex) {
            throw new JsonException(ex);
        }
    }

    public <T extends Validate> T validFromJson(String input, Class<T> tClass) throws JsonException, ValidationException {
        T result = fromJson(input, tClass);
        result.validate();
        return result;
    }

    public JsonService() {
        gson = new Gson();
    }

}
