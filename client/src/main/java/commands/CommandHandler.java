package commands;

import state.AppState;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandHandler {
    private final HashMap<String, CommandEndpoint> map;
    private final ArrayList<Command> commands;

    public static final class UnknownCommandException extends Exception {

    }

    public static final class BadContextException extends Exception {
        public BadContextException(String userMessage) {
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
            map.put(alias, command.endpoint());
        }
    }

    public void handle(AppState state, String command, String[] args) throws UnknownCommandException, BadContextException {
        CommandEndpoint endpoint =  map.get(command);
        if (endpoint == null) {
            throw new UnknownCommandException();
        }
        endpoint.handle(state, args);
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
