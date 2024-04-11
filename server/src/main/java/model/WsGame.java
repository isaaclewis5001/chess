package model;

import chess.BoardState;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Iterator;

public class WsGame {
    private final ArrayList<WsClient> clients;
    private final int id;

    private final ChessGame game;

    private String blackUsername = null;
    private String whiteUsername = null;

    private boolean gameOver = false;

    public WsGame(int id) {
        clients = new ArrayList<>();
        this.id = id;
        this.game = new ChessGame();
        this.game.getBoard().resetBoard();
    }


    public void setPlayer(ChessGame.TeamColor color, String username) {
        if (color == ChessGame.TeamColor.WHITE) {
            whiteUsername = username;
        } else {
            blackUsername = username;
        }
    }

    public int getId() {
        return id;
    }
    public void addClient(WsClient client) {
        clients.add(client);
    }

    public void removeClient(WsClient client) {
        clients.remove(client);
        if (client.username().equals(whiteUsername)) {
            // white wins by abandonment
        }
        else if (client.username().equals(blackUsername)) {
            // black wins by abandonment
        }
    }
    public Iterator<WsClient> clientsIterator() {
        return clients.listIterator();
    }

    public BoardState getBoard() {
        return game.getBoardState();
    }

}
