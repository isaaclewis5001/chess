package commands;

import state.AppState;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandHandler {
    private final HashMap<String, Command> map;
    private final ArrayList<Command> commands;

    public static final class UnknownCommandException extends Exception {

    }

    public static final class BadContextException extends Exception {
        public BadContextException(String userMessage) {
            super(userMessage);
        }
    }

    public static final class BadArgsException extends Exception {
        public BadArgsException(String userMessage) {
            super(userMessage);
        }
    }

    public CommandHandler() {
        map = new HashMap<>();
        commands = new ArrayList<>();
    }

    public void add(Command command) {
        commands.add(command);
        for (String alias: command.aliases()) {
            map.put(alias, command);
        }
    }

    public void handle(AppState state, String command, String[] args) throws UnknownCommandException, BadContextException, BadArgsException {
        Command commandObject =  map.get(command);
        if (commandObject == null) {
            throw new UnknownCommandException();
        }
        commandObject.endpoint().handle(state, args);
    }

    public Command getCommand(String name) {
        return map.get(name);
    }


    public void printCommands(AppState state, PrintStream out) {
        for (Command command: commands) {
            if (command.endpoint().validInContext(state)) {
                command.printUsage(out);
                out.println();
            }
        }
    }
}
