package driver;

import commands.CommandHandler;
import state.AppState;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Scanner;

public class AppDriver implements Runnable {
    private final AppState state;
    public AppDriver(AppState state) {
        this.state = state;
    }


    public void run() {
        Scanner input = new Scanner(System.in);
        while(!state.shouldQuit()) {
            String command = input.nextLine();
            String[] words = command.split(" ");
            if (words.length == 0) {
                continue;
            }
            try {
                state.handler.handle(state, words[0], Arrays.copyOfRange(words, 1, words.length));
            } catch (CommandHandler.BadContextException ex) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println("Command \"" + words[0] + "\" is not available right now:");
                System.out.println(ex.getMessage());
            } catch (CommandHandler.UnknownCommandException ex) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println("Unknown command \"" + words[0] + "\".");
            } catch (CommandHandler.BadArgsException ex) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println("Bad usage of \"" + words[0] + "\":");
                System.out.println(ex.getMessage());
            }
        }
    }
}
