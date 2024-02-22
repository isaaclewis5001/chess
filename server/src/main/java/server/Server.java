package server;

import spark.*;

public class Server {
    private final Handlers handlers;


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");


        Spark.init();
        Spark.awaitInitialization();

        Spark.delete("/db", handlers.clearHandler()::clear);
        Spark.post("/user", handlers.registrationHandler()::createUser);

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Server() {
        DataAccess dataAccess = new MemoryDataAccess();
        Services services = new Services(dataAccess);
        this.handlers = new Handlers(services);
    }
}
