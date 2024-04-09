package wsHandler;

import javax.websocket.Session;

public record WsClient(String authToken, String username, Session session, WsGame game) {}
