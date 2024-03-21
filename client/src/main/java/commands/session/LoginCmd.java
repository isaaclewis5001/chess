package commands.session;

import commands.CommandEndpoint;
import commands.CommandHandler;
import server.LoginException;
import server.ServerException;
import state.AppState;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class LoginCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {"[username]", "[password]"};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "attempts to log in to the server with the provided credentials.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 2) {
            throw new CommandHandler.BadArgsException("Expected 2 inputs, found " + inputs.length + ".");
        }
        if (!validInContext(state)) {
            throw new CommandHandler.BadContextException("You are already logged in.");
        }
        try {
            state.loginState = state.serverFacade.login(inputs[0], inputs[1]);
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
            return;
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
            return;
        } catch (LoginException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Invalid login credentials.");
        }

        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Logged in successfully!");
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.loginState == null;
    }
}
