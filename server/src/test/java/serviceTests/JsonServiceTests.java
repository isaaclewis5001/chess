package serviceTests;

import com.google.gson.GsonBuilder;
import model.validation.Validate;
import model.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.JsonService;

@DisplayName("Json Service Tests")
public class JsonServiceTests {
    private static JsonService createService() {
        return new JsonService(new GsonBuilder());
    }


    private record AscendingSequence(int a, int b, int c) implements Validate {
        @Override
        public void validate() throws ValidationException {
            if (a > b || b > c) {
                throw new ValidationException("not ascending");
            }
        }
    }

    @Test
    @DisplayName("From Json (No Validation)")
    public void fromJson() throws Exception {
        JsonService service = createService();
        AscendingSequence result = service.fromJson("{\"a\"=2, \"b\"=4, \"c\"=3}", AscendingSequence.class);
        Assertions.assertEquals(new AscendingSequence(2, 4, 3), result);
    }

    @Test
    @DisplayName("Bad Json")
    public void badJson() {
        JsonService service = createService();
        Assertions.assertThrows(JsonService.JsonException.class,
            () -> service.fromJson("{\"a\"=2, \"b\"=4, \"c\"=3", AscendingSequence.class)
        );
    }

    @Test
    @DisplayName("Valid Value")
    public void validValue() throws Exception {
        JsonService service = createService();
        AscendingSequence result = service.validFromJson("{\"a\"=2, \"b\"=3, \"c\"=4}", AscendingSequence.class);
        Assertions.assertEquals(new AscendingSequence(2, 3, 4), result);
    }

    @Test
    @DisplayName("Invalid Value")
    public void invalidValue() {
        JsonService service = createService();
        Assertions.assertThrows(ValidationException.class,
                () -> service.validFromJson("{\"a\"=2, \"b\"=4, \"c\"=3}", AscendingSequence.class)
        );
    }

    @Test
    @DisplayName("To Json")
    public void toJson() throws Exception {
        JsonService service = createService();
        AscendingSequence value = new AscendingSequence(2, 3, 4);
        AscendingSequence afterRoundTrip = service.fromJson(service.toJson(value), AscendingSequence.class);
        Assertions.assertEquals(value, afterRoundTrip);
    }
}
