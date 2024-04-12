package commands.gameplay;

import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import state.GameState;

public class DrawCmd implements CommandEndpoint {
    @Override
    public String[] argumentNames() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Redraws the current board.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 0) {
            throw new CommandHandler.BadArgsException("Unexpected command arguments");
        }
        GameState gs = state.getGameState();
        if (gs == null) {
            throw new CommandHandler.BadContextException("You cannot draw the board while you are not playing or observing.");
        }

        StringBuilder out = new StringBuilder();
        gs.draw(out, null, null);
        System.out.println(out);
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.getGameState() != null;
    }
}
