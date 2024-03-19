package state;

import server.ServerFacade;
import commands.CommandHandler;

public class AppState {
    public final CommandHandler handler;
    public final ServerFacade serverFacade;

    public LoginState loginState;

    public AppState(CommandHandler handler, ServerFacade serverFacade) {
        this.handler = handler;
        this.serverFacade = serverFacade;
    }
}
