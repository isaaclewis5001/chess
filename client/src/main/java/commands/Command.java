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
                out.println(" / ");
            }
            out.print(alias);
        }
        out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY + " :");
        String[] params = endpoint.argumentNames();

        for (String param: params) {
            out.print(" ");
            out.print(param);
        }

        out.println(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        out.println(endpoint.getDescription());
    }
}
