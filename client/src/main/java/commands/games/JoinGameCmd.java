package commands.games;

import chess.ChessBoard;
import chess.ChessGame;
import commands.CommandEndpoint;
import commands.CommandHandler;
import model.data.GameDesc;
import server.BadGameIdException;
import server.ServerException;
import server.TeamTakenException;
import server.UnauthorizedException;
import state.AppState;
import ui.BoardDrawing;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class JoinGameCmd implements CommandEndpoint {
    private static final String[] JOIN_ARG_NAMES = {"[game number]", "[WHITE/BLACK]"};
    private static final String[] WATCH_ARG_NAMES = {"[game number]"};

    private final boolean isJoin;

    public JoinGameCmd(boolean isJoin) {
        this.isJoin = isJoin;
    }

    @Override
    public String[] argumentNames() {
        return isJoin ? JOIN_ARG_NAMES : WATCH_ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return isJoin ? "Attempts to join a game as a player." :  "Attempts to watch an ongoing game.";
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

        if (state.loginState == null) {
            throw new CommandHandler.BadContextException("You must be logged in to enter a game.");
        }
        else if (state.gamesList == null) {
            throw new CommandHandler.BadContextException("Please list the ongoing games before attempting to enter one.");
        }

        GameDesc desc;
        try {
            desc = state.gamesList.get(gameNumber);
        } catch (IndexOutOfBoundsException ex) {
            throw new CommandHandler.BadArgsException("Game number out of range. Please refer to the listed games.");
        }

        try {
            state.serverFacade.joinGame(state.loginState, desc.gameID(), team);

            ChessBoard board = new ChessBoard();

            BoardDrawing.draw(board, ChessGame.TeamColor.WHITE, null);
            BoardDrawing.draw(board, ChessGame.TeamColor.BLACK, null);
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
        } catch (UnauthorizedException ex) {
            CommonMessages.badAuth();
            state.loginState = null;
        } catch (TeamTakenException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "The desired team is already taken. Try refreshing your list of games.");
        } catch (BadGameIdException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "The requested game no longer exists.");
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.gamesList != null && state.loginState != null;
    }
}
