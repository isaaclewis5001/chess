package wsHandler;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.GameplayService;
import service.JsonService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.userCommands.*;

import java.io.IOException;

public class WsHandler {
    private final JsonService jsonService;
    private final UserService userService;
    private final GameplayService gameplayService;

    private void onJoin(Session session, JoinPlayerUGC command) {

    }

    private void onObserve(Session session, JoinObserverUGC command) {

    }

    private void onMove(Session session, MakeMoveUGC command) {

    }
    private void onLeave(Session session, LeaveUGC command) {

    }

    private void onResign(Session session, ResignUGC command) {

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

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                if (command instanceof JoinPlayerUGC joinCommand) {
                    onJoin(session, joinCommand);
                    return;
                }
            }
            case JOIN_OBSERVER -> {
                if (command instanceof JoinObserverUGC observeCommand) {
                    onObserve(session, observeCommand);
                    return;
                }
            }
            case MAKE_MOVE -> {
                if (command instanceof MakeMoveUGC moveCommand) {
                    onMove(session, moveCommand);
                    return;
                }
            }
            case LEAVE -> {
                if (command instanceof LeaveUGC leaveCommand) {
                    onLeave(session, leaveCommand);
                    return;
                }
            }
            case RESIGN -> {
                if (command instanceof ResignUGC resignCommand) {
                    onResign(session, resignCommand);
                    return;
                }
            }
        }
        sendErrorMessage(session,"malformed WebSocket request");
    }

    public void sendErrorMessage(Session session, String errorMessage) {
        ErrorSM messageObject = new ErrorSM("Error: " + errorMessage);
        try {
            session.getRemote().sendString(jsonService.toJson(messageObject));
        } catch (IOException exception) {
            // The client is insane and unreceptive?? Dear me...
        }
    }

    public WsHandler(JsonService jsonService, UserService userService, GameplayService gameplayService) {
        this.jsonService = jsonService;
        this.userService = userService;
        this.gameplayService = gameplayService;
    }
}
