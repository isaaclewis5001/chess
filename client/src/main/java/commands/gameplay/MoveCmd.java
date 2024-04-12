package commands.gameplay;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import state.GameState;
import ui.CommonMessages;

import java.io.IOException;

public class MoveCmd implements CommandEndpoint {
    @Override
    public String[] argumentNames() {
        return new String[] {"[start]", "[end]", "<promotion>"};
    }

    @Override
    public String getDescription() {
        return "Makes a move";
    }

    static ChessPosition parseSquare(String text) throws CommandHandler.BadArgsException {
        if (text.length() != 2) {
            throw new CommandHandler.BadArgsException("Moves have the form [file][rank], e.g. c4");
        }

        int rank = text.charAt(1) - '0';
        if (rank <= 0 || rank > 8) {
            throw new CommandHandler.BadArgsException("Rank must be in the range 1-8");
        }

        int file = Character.toLowerCase(text.charAt(0)) - 'a' + 1;
        if (file <= 0 || file > 8) {
            throw new CommandHandler.BadArgsException("File must be in the range a-h");
        }

        return new ChessPosition(rank, file);
    }


    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length > 3 || inputs.length < 2) {
            throw new CommandHandler.BadArgsException("Expected two or three arguments");
        }

        ChessPosition start = parseSquare(inputs[0]);
        ChessPosition end = parseSquare(inputs[1]);
        ChessPiece.PieceType type = null;
        if (inputs.length == 3) {
            try {
                type = ChessPiece.PieceType.valueOf(inputs[2]);
            } catch (IllegalArgumentException ex) {
                throw new CommandHandler.BadArgsException("Invalid piece type");
            }
        }

        ChessMove move = new ChessMove(start, end, type);
        GameState gs = state.getGameState();
        if (gs == null) {
            throw new CommandHandler.BadContextException("You must be in a game to make moves.");
        }
        else if (gs.getGame().getTeamTurn() != gs.getTeam()) {
            throw new CommandHandler.BadContextException("It is not your turn to play.");
        } else if (!gs.getGame().isMoveValid(move)) {
            throw new CommandHandler.BadArgsException("The requested move is illegal.");
        }

        try {
            gs.move(move);
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.getGameState() != null;
    }
}
