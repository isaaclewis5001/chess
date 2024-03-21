package commands.general;

import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;

public class QuitCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Exits the program.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        state.quit();
    }

    @Override
    public boolean validInContext(AppState state) {
        return true;
    }
}
