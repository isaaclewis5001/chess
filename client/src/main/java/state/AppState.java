package state;

import server.ServerFacade;
import commands.CommandHandler;

public class AppState {
    public final CommandHandler handler;
    public final ServerFacade serverFacade;
    public LoginState loginState;

    public ListedGames gamesList;

    private boolean quit;

    public void quit() {
        quit = true;
    }
    public boolean shouldQuit() {
        return quit;
    }
    public AppState(CommandHandler handler, ServerFacade serverFacade) {
        this.handler = handler;
        this.serverFacade = serverFacade;
        this.gamesList = null;
        this.loginState = null;
    }
}
