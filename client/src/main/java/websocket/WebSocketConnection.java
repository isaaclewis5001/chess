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

    public<Receive> WebSocketConnection(String wsServerURL, Consumer<Receive> handler, Class<Receive> receiveClass) throws IOException, DeploymentException {
        URI uri = URI.create(wsServerURL + "/connect");
        this.wsSession = ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
        this.gson = Adapters.getGsonBuilder().create();
        this.wsSession.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                Receive receivedObject;
                try {
                    receivedObject = gson.fromJson(message, receiveClass);
                } catch (JsonSyntaxException exception) {
                    System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error parsing server message");
                    return;
                }
                handler.accept(receivedObject);
            }
        });
    }

    public<Send> void send(Send command) throws IOException {
        String message = gson.toJson(command);
        this.wsSession.getBasicRemote().sendText(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {

    }
}
