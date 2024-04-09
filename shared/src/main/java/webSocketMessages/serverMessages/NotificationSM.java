package webSocketMessages.serverMessages;

public class NotificationSM extends ServerMessage {

    private final String message;
    public NotificationSM(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
