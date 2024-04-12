package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import gson.Adapters;
import ui.EscapeSequences;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

public class WebSocketConnection extends Endpoint {
    private final Session wsSession;
    private final Gson gson;

    public<R> WebSocketConnection(String wsServerURL, Consumer<R> handler, Class<R> receiveClass) throws IOException, DeploymentException {
        URI uri = URI.create(wsServerURL + "/connect");
        this.wsSession = ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
        this.gson = Adapters.getGsonBuilder().create();
        this.wsSession.addMessageHandler((MessageHandler.Whole<String>) message -> {
            R receivedObject;
            try {
                receivedObject = gson.fromJson(message, receiveClass);
            } catch (JsonSyntaxException exception) {
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error parsing server message");
                return;
            }
            handler.accept(receivedObject);
        });
    }

    public<S> void send(S command) throws IOException {
        String message = gson.toJson(command);
        this.wsSession.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {

    }
}
