package commands.gameplay;

import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import state.GameState;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class LeaveCmd implements CommandEndpoint {
    @Override
    public String[] argumentNames() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Disconnects you from a game.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 0) {
            throw new CommandHandler.BadArgsException("Unexpected command arguments");
        }

        GameState gs = state.getGameState();
        if (gs == null) {
            throw new CommandHandler.BadContextException("You must be in a game to leave it.");
        }

        try {
            gs.leave();
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Left game.");
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
            System.out.println("(Disconnecting anyway)");
        }
        state.setGameState(null);

    }

    @Override
    public boolean validInContext(AppState state) {
        return state.getGameState() != null;
    }
}
