package state;

import server.ServerFacade;
import commands.CommandHandler;

public class AppState {
    public final CommandHandler handler;
    public final ServerFacade serverFacade;
    private LoginState loginState;
    private ListedGames gamesList;
    private GameState gameState;

    private boolean quit;

    public void quit() {
        logout();
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
        this.gameState = null;
    }

    public void logout() {
        loginState = null;
        gamesList = null;
        gameState = null;
    }

    public boolean isLoggedIn() {
        return loginState != null;
    }

    public LoginState loginState() {
        return loginState;
    }

    public void setLoginState(LoginState loginState) {
        this.loginState = loginState;
    }


    public void setGamesList(ListedGames gamesList) {
        this.gamesList = gamesList;
    }

    public boolean areGamesListed() {
        return this.gamesList != null;
    }

    public ListedGames gamesList() {
        return gamesList;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public GameState getGameState() {
        return gameState;
    }
}
