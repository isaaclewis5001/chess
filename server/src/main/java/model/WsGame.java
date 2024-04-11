package model;

import chess.*;

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

        ChessBoard board = new ChessBoard();
        board.resetBoard();
        this.game = new ChessGame(board);
    }

    public boolean makeMove(ChessMove move) {
        if (gameLocked()) {
            return false;
        }
        try {
            game.makeMove(move);
        } catch (InvalidMoveException ex) {
            return false;
        }
        gameOver = game.isInCheckmate(game.getTeamTurn()) || game.isInStalemate(game.getTeamTurn());
        return true;
    }

    public boolean gameLocked() {
        return gameOver || whiteUsername == null || blackUsername == null;
    }

    public String getPlayerToMove() {
        if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
            return whiteUsername;
        } else {
            return blackUsername;
        }
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

    public boolean resign(String username) {
        if (gameOver) {
            return false;
        }
        if (username.equals(whiteUsername) || username.equals(blackUsername)) {
            gameOver = true;
            return true;
        }
        return false;
    }

    public void removeClient(WsClient client) {
        clients.remove(client);
        resign(client.username());
    }
    public Iterator<WsClient> clientsIterator() {
        return clients.listIterator();
    }

    public BoardState getBoard() {
        return game.getBoardState();
    }

}
