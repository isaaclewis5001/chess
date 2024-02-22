package model.response;

public record ErrorResponse(String message) {
    public ErrorResponse(String message) {
        this.message = "Error: " + message;
    }
}
