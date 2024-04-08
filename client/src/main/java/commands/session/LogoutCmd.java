package commands.session;

import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import ui.EscapeSequences;

public class LogoutCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Logs out of the active account.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 0) {
            throw new CommandHandler.BadArgsException("Unexpected arguments after command");
        }
        if (!validInContext(state)) {
            throw new CommandHandler.BadContextException("You are not logged in.");
        }
        if (state.serverFacade.logout(state.loginState())) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Logged out successfully!");
        }
        else {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "The server had an issue logging you out.");
            System.out.println("You are no longer logged in locally.");
        }
        state.logout();
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.isLoggedIn();
    }
}
