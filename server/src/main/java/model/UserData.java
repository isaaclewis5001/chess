package model;

public record UserData(String username, String password, String email) {
    public boolean validateFields() {
        if (
            username == null ||
            password == null ||
            email == null
        ) {
            return false;
        }

        return !(
            username.isEmpty() ||
            password.isEmpty() ||
            email.isEmpty()
        );
    }

}
