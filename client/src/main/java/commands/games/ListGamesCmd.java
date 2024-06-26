package commands.games;

import commands.CommandEndpoint;
import commands.CommandHandler;
import model.data.GameDesc;
import server.ServerException;
import server.UnauthorizedException;
import state.AppState;
import state.ListedGames;
import ui.CommonMessages;

import java.io.IOException;
import java.util.List;

public class ListGamesCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Fetches a list of all active games.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 0) {
            throw new CommandHandler.BadArgsException("Unexpected inputs after command.");
        }
        if (!state.isLoggedIn()) {
            throw new CommandHandler.BadContextException("You must be logged in to see the active games.");
        } else if (state.getGameState() != null) {
            throw new CommandHandler.BadContextException("You cannot list games while playing or observing.ing ");
        }
        try {
            List<GameDesc> games = state.serverFacade.listGames(state.loginState());
            state.setGamesList(new ListedGames(games));
            state.gamesList().display();
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        } catch (ServerException ex) {
            CommonMessages.serverException(ex);
        } catch (UnauthorizedException ex) {
            CommonMessages.badAuth();
            state.logout();
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.isLoggedIn() && state.getGameState() == null;
    }
}
