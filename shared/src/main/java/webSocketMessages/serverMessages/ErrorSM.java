package webSocketMessages.serverMessages;

public class ErrorSM extends ServerMessage {
    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorSM(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
}
