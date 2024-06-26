package server;

import dataAccess.bundles.DataAccessBundle;
import dataAccess.bundles.SQLDataAccessBundle;
import gson.Adapters;
import handler.HandlerBundle;
import service.ServiceBundle;
import spark.*;


public class Server {
    private final HandlerBundle handlerBundle;


    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");


        Spark.webSocket("/connect", handlerBundle.webSocketHandler());

        Spark.init();
        Spark.awaitInitialization();

        Spark.delete("/db", handlerBundle.clearHandler()::clear);
        Spark.post("/user", handlerBundle.registrationHandler()::createUser);
        Spark.post("/session", handlerBundle.sessionHandler()::login);
        Spark.delete("/session", handlerBundle.sessionHandler()::logout);
        Spark.get("/game", handlerBundle.gamesHandler()::listGames);
        Spark.post("/game", handlerBundle.gamesHandler()::createGame);
        Spark.put("/game", handlerBundle.gamesHandler()::joinGame);

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public Server(DataAccessBundle dataAccessBundle) {
        ServiceBundle serviceBundle = new ServiceBundle(dataAccessBundle, Adapters.getGsonBuilder());
        this.handlerBundle = new HandlerBundle(serviceBundle);
    }

    public Server() {
        this(new SQLDataAccessBundle());
    }
}
