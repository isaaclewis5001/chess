package wsHandler;

import chess.BoardState;
import dataAccess.DatabaseException;
import dataAccess.DuplicateKeyException;
import model.WsClient;
import model.WsGame;
import model.data.GameDesc;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GamesService;
import service.JsonService;
import service.UserService;
import service.WsGameplayService;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.serverMessages.LoadGameSM;
import webSocketMessages.serverMessages.NotificationSM;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WsHandler {
    private final JsonService jsonService;
    private final GamesService gamesService;
    private final WsGameplayService gameplayService;

    private WsClient addUser(Session session, UserGameCommand command) {
        try {
            return gameplayService.addClient(session, command);
        } catch (UserService.BadAuthException exception) {
            sendErrorMessage(session, "bad auth token");
        } catch (DatabaseException exception) {
            sendErrorMessage(session, "auth failed: database unavailable");
        } catch (DuplicateKeyException exception) {
            sendErrorMessage(session, "join game failed: already joined");
        }
        return null;
    }

    private WsClient getUser(Session session, UserGameCommand command) {
        WsClient client = gameplayService.getClient(command.getAuthString());
        if (client == null) {
            sendErrorMessage(session, "this session has not been added to a game yet; use join or observe");
            return null;
        }
        else if (command.getGameID() != client.gameId()) {
            sendErrorMessage(client.session(),"mismatched game IDs");
            return null;
        }
        return client;
    }

    private void removeUser(WsClient client) {
        gameplayService.removeClient(client);
        client.session().close();
    }

    private void onJoin(WsClient client, JoinPlayerUGC command) {
        GameDesc desc;
        try {
            desc = gamesService.fetchGame(command.getGameID());
        } catch (DatabaseException ex) {
            sendErrorMessage(client.session(), "join game failed: database error");
            removeUser(client);
            return;
        } catch (GamesService.BadGameIdException ex) {
            sendErrorMessage(client.session(), "game not found");
            removeUser(client);
            return;
        }
        String expectedUser = desc.getPlayerUsername(command.getPlayerColor());
        if (expectedUser == null || !expectedUser.equals(client.username())) {
            sendErrorMessage(client.session(), "cannot join team");
            removeUser(client);
            return;
        }
        WsGame game = gameplayService.getOrCreateGame(command.getGameID());
        game.addClient(client);
        game.setPlayer(command.getPlayerColor(), client.username());
        sendGameState(client, game.getBoard());

        game.clientsIterator().forEachRemaining((WsClient otherClient) -> {
            if (!client.username().equals(otherClient.username())) {
                sendNotification(otherClient, "join " + command.getPlayerColor() + " " + client.username());
            }
        });
    }

    private void onObserve(WsClient client, JoinObserverUGC command) {
        boolean gameExists;
        try {
            gameExists = gamesService.gameExists(command.getGameID());
        } catch (DatabaseException ex) {
            sendErrorMessage(client.session(), "join game failed: database error");
            removeUser(client);
            return;
        }
        if (!gameExists) {
            sendErrorMessage(client.session(),"cannot join game: does not exist");
            removeUser(client);
            return;
        }
        WsGame game = gameplayService.getOrCreateGame(command.getGameID());
        game.addClient(client);

        sendGameState(client, game.getBoard());

        game.clientsIterator().forEachRemaining((WsClient otherClient) -> {
            if (!client.username().equals(otherClient.username())) {
                sendNotification(otherClient, "obs " + client.username());
            }
        });
    }

    private void onMove(WsClient client, MakeMoveUGC command) {
        WsGame game = gameplayService.getGame(client.gameId());
        if (game == null) {
            sendErrorMessage(client.session(), "game does not exist");
            return;
        }
        if (game.gameLocked()) {
            sendErrorMessage(client.session(), "game is inactive");
            return;
        }
        if (!client.username().equals(game.getPlayerToMove())) {
            sendErrorMessage(client.session(), "not your turn");
            return;
        }

        if (!game.makeMove(command.getMove())) {
            sendErrorMessage(client.session(), "invalid move");
            return;
        }

        game.clientsIterator().forEachRemaining((WsClient otherClient) -> {
            sendGameState(otherClient, game.getBoard());
            if (!client.username().equals(otherClient.username())) {
                sendNotification(otherClient, "move " + jsonService.toJson(command.getMove()));
            }
        });
    }
    private void onLeave(WsClient client) {
        WsGame game = gameplayService.getGame(client.gameId());
        if (game == null) {
            return;
        }

        removeUser(client);

        game.clientsIterator().forEachRemaining((WsClient otherClient) -> {
            if (!client.username().equals(otherClient.username())) {
                sendNotification(otherClient, "leave " + client.username());
            }
        });
    }

    private void onResign(WsClient client) {
        WsGame game = gameplayService.getGame(client.gameId());
        if (game == null) {
            sendErrorMessage(client.session(), "game does not exist");
            return;
        }

        if (!game.resign(client.username())) {
            sendErrorMessage(client.session(), "resign unavailable");
            return;
        }

        game.clientsIterator().forEachRemaining((WsClient otherClient) ->
            sendNotification(otherClient, "resign " + client.username())
        );
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand command;
        try {
            command = jsonService.fromJson(message, UserGameCommand.class);
        } catch (JsonService.JsonException exception) {
            sendErrorMessage(session, "malformed WebSocket request");
            return;
        }

        UserGameCommand.CommandType commandType = command.getCommandType();

        switch (commandType) {
            case JOIN_PLAYER -> {
                if (command instanceof JoinPlayerUGC joinCommand) {
                    WsClient client = addUser(session, command);
                    if (client != null) {
                        onJoin(client, joinCommand);
                    }
                    return;
                }
            }
            case JOIN_OBSERVER -> {
                if (command instanceof JoinObserverUGC observeCommand) {
                    WsClient client = addUser(session, command);
                    if (client != null) {
                        onObserve(client, observeCommand);
                    }
                    return;
                }
            }
            case MAKE_MOVE -> {
                if (command instanceof MakeMoveUGC moveCommand) {
                    WsClient client = getUser(session, command);
                    if (client != null) {
                        onMove(client, moveCommand);
                    }
                    return;
                }
            }
            case LEAVE -> {
                if (command instanceof LeaveUGC) {
                    WsClient client = getUser(session, command);
                    if (client != null) {
                        onLeave(client);
                    }
                    return;
                }
            }
            case RESIGN -> {
                if (command instanceof ResignUGC) {
                    WsClient client = getUser(session, command);
                    if (client != null) {
                        onResign(client);
                    }
                    return;
                }
            }
        }
        sendErrorMessage(session,"malformed WebSocket request");
    }

    private void sendGameState(WsClient client, BoardState board) {
        LoadGameSM messageObject = new LoadGameSM(board);
        try {
            client.session().getRemote().sendString(jsonService.toJson(messageObject));
        } catch (IOException ex) {
            removeUser(client);
        }
    }

    private void sendNotification(WsClient client, String message) {
        NotificationSM messageObject = new NotificationSM(message);
        try {
            client.session().getRemote().sendString(jsonService.toJson(messageObject));
        } catch (IOException exception) {
            removeUser(client);
        }
    }

    private void sendErrorMessage(Session session, String errorMessage) {
        ErrorSM messageObject = new ErrorSM("Error: " + errorMessage);
        try {
            session.getRemote().sendString(jsonService.toJson(messageObject));
        } catch (IOException exception) {
            // The client is insane and unreceptive?? Dear me...
        }
    }

    public WsHandler(JsonService jsonService, WsGameplayService gameplayService, GamesService gamesService) {
        this.jsonService = jsonService;
        this.gameplayService = gameplayService;
        this.gamesService = gamesService;
    }
}
