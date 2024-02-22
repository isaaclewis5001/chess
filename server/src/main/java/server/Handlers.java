package server;

import handler.ClearHandler;
import handler.RegistrationHandler;

public class Handlers {
    private final ClearHandler clearHandler;
    private final RegistrationHandler registrationHandler;
    public Handlers(Services services) {
        clearHandler = new ClearHandler(services.clearService());
        registrationHandler = new RegistrationHandler(services.userService());
    }

    public ClearHandler clearHandler() {
        return clearHandler;
    }

    public RegistrationHandler registrationHandler() {
        return registrationHandler;
    }
}
