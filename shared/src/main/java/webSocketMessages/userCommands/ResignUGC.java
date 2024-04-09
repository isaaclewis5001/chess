package webSocketMessages.userCommands;

public class ResignUGC extends UserGameCommand {
    public ResignUGC(String authToken, int gameId) {
        super(authToken, gameId);
        this.commandType = CommandType.RESIGN;
    }
}
