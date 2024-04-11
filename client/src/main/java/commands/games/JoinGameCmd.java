package commands.games;

import chess.ChessBoard;
import chess.ChessGame;
import commands.CommandEndpoint;
import commands.CommandHandler;
import state.GameState;
import model.data.GameDesc;
import server.BadGameIdException;
import server.ServerException;
import server.TeamTakenException;
import server.UnauthorizedException;
import state.AppState;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class JoinGameCmd implements CommandEndpoint {
    private static final String[] JOIN_ARG_NAMES = {"[game number]", "[WHITE/BLACK]"};
    private static final String[] WATCH_ARG_NAMES = {"[game number]"};

    private final boolean isJoin;
    private final String wsURL;

    public JoinGameCmd(boolean isJoin, String wsURL) {
        this.isJoin = isJoin;
        this.wsURL = wsURL;
    }

    @Override
    public String[] argumentNames() {
        return isJoin ? JOIN_ARG_NAMES : WATCH_ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return isJoin ? "Attempts to join a game as a player." :  "Attempts to watch an ongoing game.";
    }


    private void connectToGame(AppState state, GameDesc desc, ChessGame.TeamColor team) {
        GameState gameState;
        try {
            gameState = new GameState(wsURL, state.loginState(), desc);
        } catch (Exception ex) {
            CommonMessages.issueConnecting();
            return;
        }

        try {
            if (team == null) {
                gameState.watch();
            } else {
                gameState.join(team);
            }
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        }

        state.setGameState(gameState);
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (isJoin && inputs.length != 2) {
            throw new CommandHandler.BadArgsException("Expected two inputs, found " + inputs.length + ".");
        }
        else if (!isJoin && inputs.length != 1) {
            throw new CommandHandler.BadArgsException("Expected one input, found " + inputs.length + ".");
        }

        int gameNumber;
        try {
            gameNumber = Integer.parseInt(inputs[0]);
        } catch (NumberFormatException ex) {
            throw new CommandHandler.BadArgsException("Expected a number, found " + inputs[0] + ".");
        }

        ChessGame.TeamColor team = null;
        if (isJoin) {
            if (inputs[1].equalsIgnoreCase("white")) {
                team = ChessGame.TeamColor.WHITE;
            } else if (inputs[1].equalsIgnoreCase("black")) {
                team = ChessGame.TeamColor.BLACK;
            } else {
                throw new CommandHandler.BadArgsException("The second input should either be WHITE or BLACK.");
            }
        }

        if (!state.isLoggedIn()) {
            throw new CommandHandler.BadContextException("You must be logged in to enter a game.");
        }
        else if (!state.areGamesListed()) {
            throw new CommandHandler.BadContextException("Please list the ongoing games before attempting to enter one.");
        } else if (state.getGameState() != null) {
            throw new CommandHandler.BadContextException("You are already playing a game.");
        }

        GameDesc desc;
        try {
            desc = state.gamesList().get(gameNumber);
        } catch (IndexOutOfBoundsException ex) {
            throw new CommandHandler.BadArgsException("Game number out of range. Please refer to the listed games.");
        }

        try {
            state.serverFacade.joinGame(state.loginState(), desc.gameID(), team);

            ChessBoard board = new ChessBoard();
            board.resetBoard();

            connectToGame(state, desc, team);
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
        } catch (UnauthorizedException ex) {
            CommonMessages.badAuth();
            state.logout();
        } catch (TeamTakenException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "The desired team is already taken. Try refreshing your list of games.");
        } catch (BadGameIdException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "The requested game no longer exists.");
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.areGamesListed() && state.getGameState() == null;
    }
}
