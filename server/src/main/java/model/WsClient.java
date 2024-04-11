package model;


import org.eclipse.jetty.websocket.api.Session;

public record WsClient(String authToken, String username, Session session, int gameId) {}
