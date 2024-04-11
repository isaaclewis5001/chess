package gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import webSocketMessages.serverMessages.ErrorSM;
import webSocketMessages.serverMessages.LoadGameSM;
import webSocketMessages.serverMessages.NotificationSM;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

public class Adapters {

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(UserGameCommand.class, (JsonDeserializer<UserGameCommand>)(jsonElement, typeOf, context) -> {
            UserGameCommand ugc = null;
            if (jsonElement.isJsonObject()) {
                JsonElement commandType = jsonElement.getAsJsonObject().get("commandType");
                if (commandType.isJsonPrimitive()) {
                    switch (UserGameCommand.CommandType.valueOf(commandType.getAsString())) {
                        case JOIN_PLAYER -> ugc = context.deserialize(jsonElement, JoinPlayerUGC.class);
                        case JOIN_OBSERVER -> ugc = context.deserialize(jsonElement, JoinObserverUGC.class);
                        case MAKE_MOVE -> ugc = context.deserialize(jsonElement, MakeMoveUGC.class);
                        case LEAVE -> ugc = context.deserialize(jsonElement, LeaveUGC.class);
                        case RESIGN -> ugc = context.deserialize(jsonElement, ResignUGC.class);
                    }
                }
            }
            return ugc;
        });
        gb.registerTypeAdapter(ServerMessage.class, (JsonDeserializer<ServerMessage>)(jsonElement, typeOf, context) -> {
            ServerMessage sm = null;
            if (jsonElement.isJsonObject()) {
                JsonElement commandType = jsonElement.getAsJsonObject().get("serverMessageType");
                if (commandType.isJsonPrimitive()) {
                    switch (ServerMessage.ServerMessageType.valueOf(commandType.getAsString())) {
                        case LOAD_GAME -> sm = context.deserialize(jsonElement, LoadGameSM.class);
                        case NOTIFICATION -> sm = context.deserialize(jsonElement, NotificationSM.class);
                        case ERROR -> sm = context.deserialize(jsonElement, ErrorSM.class);
                    }
                }
            }
            return sm;
        });
        return gb;
    }
    private Adapters() {}
}
