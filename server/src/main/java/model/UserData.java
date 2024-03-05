package model;

import model.request.CreateUserRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record UserData(String username, String hashedPassword, String email) {
    public UserData(CreateUserRequest createUserRequest, BCryptPasswordEncoder encoder) {
        this(
            createUserRequest.username(),
            encoder.encode(createUserRequest.password()),
            createUserRequest.email()
        );
    }

    public boolean passwordMatches(String password, BCryptPasswordEncoder encoder) {
        return encoder.matches(password, hashedPassword);
    }
}
