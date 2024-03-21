package commands.general;

import commands.Command;
import commands.CommandEndpoint;
import commands.CommandHandler;
import state.AppState;
import ui.EscapeSequences;

public class HelpCmd implements CommandEndpoint {
    private static final String[] ARG_NAMES = {"<command>"};
    @Override
    public String[] argumentNames() {
        return ARG_NAMES;
    }

    @Override
    public String getDescription() {
        return "Displays information about the given command, or lists all commands if none is provided.";
    }

    @Override
    public void handle(AppState state, String[] inputs) throws CommandHandler.BadArgsException {
        if (inputs.length == 0) {
            state.handler.printCommands(state, System.out);
        }
        else if (inputs.length > 1) {
            throw new CommandHandler.BadArgsException("Too many arguments");
        }
        else {
            String commandName = inputs[0];
            Command command = state.handler.getCommand(commandName);
            if (command == null) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println("Unrecognized command \"" + commandName + "\".");
                return;
            }
            command.printUsage(System.out);
        }
    }

    @Override
    public boolean validInContext(AppState state) {
        return true;
    }
}
