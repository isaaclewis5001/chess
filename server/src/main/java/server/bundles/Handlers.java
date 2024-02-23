package server.bundles;

import handler.ClearHandler;
import handler.RegistrationHandler;
import handler.SessionHandler;

public class Handlers {
    private final ClearHandler clearHandler;
    private final SessionHandler sessionHandler;
    private final RegistrationHandler registrationHandler;
    public Handlers(Services services) {
        clearHandler = new ClearHandler(services.clearService());
        registrationHandler = new RegistrationHandler(services.userService(), services.jsonService());
        sessionHandler = new SessionHandler(services.userService(), services.jsonService());
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
}
