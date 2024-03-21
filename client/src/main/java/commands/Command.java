package commands;

import ui.EscapeSequences;

import java.io.PrintStream;

public record Command(String[] aliases, CommandEndpoint endpoint) {

    public void printUsage(PrintStream out) {
        out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        boolean first = true;
        for (String alias: aliases) {
            if (first) {
                first = false;
            }
            else {
                out.print(" / ");
            }
            out.print(alias);
        }

        String[] params = endpoint.argumentNames();
        if (params.length > 0) {
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + ":");

            for (String param : params) {
                out.print(" ");
                out.print(param);
            }
        }
        out.println(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        out.println(endpoint.getDescription());
    }
}
