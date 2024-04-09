package webSocketMessages.userCommands;

public class LeaveUGC extends UserGameCommand {
    public LeaveUGC(String authToken, int gameId) {
        super(authToken, gameId);
        this.commandType = CommandType.LEAVE;
    }
}
