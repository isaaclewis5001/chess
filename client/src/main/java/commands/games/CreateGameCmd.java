package commands.games;

import commands.CommandEndpoint;
import commands.CommandHandler;
import server.ServerException;
import server.UnauthorizedException;
import state.AppState;
import ui.CommonMessages;
import ui.EscapeSequences;

import java.io.IOException;

public class CreateGameCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {"[name]"};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Creates a new game with the specified name.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length == 0) {
            throw new CommandHandler.BadArgsException("Please provide a name for your game.");
        } else if (inputs.length > 1) {
            throw new CommandHandler.BadArgsException("Expected 1 input, found " + inputs.length + ".");
        }
        if (state.loginState == null) {
            throw new CommandHandler.BadContextException("You must be logged in to create games.");
        }
        try {
            state.serverFacade.createGame(state.loginState, inputs[0]);
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Game created successfully!");
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
        } catch (UnauthorizedException ex) {
            CommonMessages.badAuth();
            state.loginState = null;
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.loginState != null;
    }
}
