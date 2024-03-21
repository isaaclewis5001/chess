package commands.session;

import commands.CommandEndpoint;
import commands.CommandHandler;
import server.ServerException;
import server.UsernameInUseException;
import state.AppState;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class RegisterCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {"[username]", "[email]", "[password]"};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Registers a new user account.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 3) {
            throw new CommandHandler.BadArgsException("Expected 3 inputs, found " + inputs.length + ".");
        }
        if (state.loginState != null) {
            throw new CommandHandler.BadContextException("You must be logged out to create an account.");
        }
        try {
            state.loginState = state.serverFacade.register(inputs[0], inputs[1], inputs[2]);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Account created successfully!");
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
        } catch (UsernameInUseException ex) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "The provided username is already in use.");
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.loginState == null;
    }
}
