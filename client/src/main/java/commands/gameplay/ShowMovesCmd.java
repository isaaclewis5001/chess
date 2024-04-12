package commands.gameplay;

import chess.ChessPiece;
import chess.ChessPosition;
import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import state.GameState;

public class ShowMovesCmd implements CommandEndpoint {
    @Override
    public String[] argumentNames() {
        return new String[] {"position"};
    }

    @Override
    public String getDescription() {
        return "Shows all legal moves originating from a given square.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 1) {
            throw new CommandHandler.BadArgsException("Expected one argument.");
        }
        GameState gs = state.getGameState();
        if (gs == null) {
            throw new CommandHandler.BadContextException("You must be in a game to do this.");
        }

        ChessPosition pos = MoveCmd.parseSquare(inputs[0]);
        ChessPiece highlightPiece = gs.getGame().getBoard().getPiece(pos);

        if (highlightPiece == null) {
            throw new CommandHandler.BadArgsException("The provided square is unoccupied.");
        }

        StringBuilder sb = new StringBuilder();
        gs.draw(sb, pos, highlightPiece.getTeamColor());
        System.out.println(sb);
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.getGameState() != null;
    }
}
