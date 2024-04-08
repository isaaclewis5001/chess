package state;

import server.ServerFacade;
import commands.CommandHandler;
import websocket.WebSocketConnection;

public class AppState {
    public final CommandHandler handler;
    public final ServerFacade serverFacade;
    private LoginState loginState;
    private ListedGames gamesList;
    private WebSocketConnection webSocket;

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
        this.webSocket = null;
    }

    public void logout() {
        loginState = null;
        gamesList = null;
        webSocket = null;
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




}
