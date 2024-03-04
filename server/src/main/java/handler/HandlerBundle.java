package handler;

import service.ServiceBundle;

public class HandlerBundle {
    private final ClearHandler clearHandler;
    private final SessionHandler sessionHandler;
    private final RegistrationHandler registrationHandler;
    private final GamesHandler gamesHandler;

    public HandlerBundle(ServiceBundle serviceBundle) {
        clearHandler = new ClearHandler(serviceBundle.clearService());
        registrationHandler = new RegistrationHandler(serviceBundle.userService(), serviceBundle.jsonService());
        sessionHandler = new SessionHandler(serviceBundle.userService(), serviceBundle.jsonService());
        gamesHandler = new GamesHandler(serviceBundle.userService(), serviceBundle.jsonService(), serviceBundle.gamesService());
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
}
