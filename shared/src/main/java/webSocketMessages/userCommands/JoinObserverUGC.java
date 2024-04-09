package webSocketMessages.userCommands;

public class JoinObserverUGC extends UserGameCommand {
    public JoinObserverUGC(String authToken, int gameId) {
        super(authToken, gameId);
        this.commandType = CommandType.JOIN_OBSERVER;
    }
}
