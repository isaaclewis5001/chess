package commands.gameplay;

import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import state.GameState;
import ui.CommonMessages;

import java.io.IOException;
import java.util.Scanner;

public class ResignCmd implements CommandEndpoint {
    @Override
    public String[] argumentNames() {
        return new String[0];
    }

    @Override
    public String getDescription() {
        return "Give up on the game you are playing.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException, CommandHandler.BadArgsException {
        if (inputs.length != 0) {
            throw new CommandHandler.BadArgsException("Unexpected command arguments");
        }
        GameState gs = state.getGameState();
        if (gs == null) {
            throw new CommandHandler.BadContextException("You must be in a game to resign");
        }
        if (gs.getTeam() == null) {
            throw new CommandHandler.BadContextException("You are observing");
        }
        if (gs.getGame().isGameOver()) {
            throw new CommandHandler.BadContextException("The game is already over");
        }

        System.out.println("Are you sure? (Y/N)");
        String response = new Scanner(System.in).next();
        if (!response.toLowerCase().startsWith("y")) {
            System.out.println("Did not resign.");
            return;
        }

        try {
            gs.resign();
            gs.getGame().setGameOver();
            System.out.println("Resigned.");
        } catch (IOException ex) {
            CommonMessages.issueConnecting();
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return state.getGameState() != null;
    }
}
