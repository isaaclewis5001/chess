package handler;

import service.ServiceBundle;
import wsHandler.WsHandler;

public class HandlerBundle {
    private final ClearHandler clearHandler;
    private final SessionHandler sessionHandler;
    private final RegistrationHandler registrationHandler;
    private final GamesHandler gamesHandler;
    private final WsHandler webSocketHandler;

    public HandlerBundle(ServiceBundle serviceBundle) {
        clearHandler = new ClearHandler(serviceBundle.clearService());
        registrationHandler = new RegistrationHandler(serviceBundle.userService(), serviceBundle.jsonService());
        sessionHandler = new SessionHandler(serviceBundle.userService(), serviceBundle.jsonService());
        gamesHandler = new GamesHandler(serviceBundle.userService(), serviceBundle.jsonService(), serviceBundle.gamesService());
        webSocketHandler = new WsHandler(serviceBundle.jsonService(), serviceBundle.userService(), serviceBundle.gameplayService());
    }

    public ClearHandler clearHandler() {
        return clearHandler;
    }
    public SessionHandler sessionHandler() {
        return sessionHandler;
    }

    public RegistrationHandler registrationHandler() {
        return registrationHandler;
    }

    public GamesHandler gamesHandler() {
        return gamesHandler;
    }

    public WsHandler webSocketHandler() {
        return webSocketHandler;
    }
}
