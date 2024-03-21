package state;

import model.data.GameDesc;
import ui.EscapeSequences;

import java.util.List;

public class ListedGames {
    private final List<GameDesc> games;

    public void display() {
        if (games.isEmpty()) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "No active games.");
            return;
        }
        int listNumber = 1;
        StringBuilder output = new StringBuilder();
        for (GameDesc game: games) {
            output.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            output.append(listNumber);
            output.append(". ");
            output.append(game.gameName());
            output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + "\n");
            if (game.whiteUsername() != null) {
                output.append("    " + EscapeSequences.SET_TEXT_COLOR_GREEN);
                output.append(game.whiteUsername());
                output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " as white\n");
            }
            else {
                output.append("    white available\n");
            }
            if (game.blackUsername() != null) {
                output.append("    " + EscapeSequences.SET_TEXT_COLOR_GREEN);
                output.append(game.blackUsername());
                output.append(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " as black\n");
            }
            else {
                output.append("    black available\n");
            }
            listNumber++;
        }
        System.out.print(output);
    }

    public GameDesc get(int index) {
        return games.get(index - 1);
    }

    public ListedGames(List<GameDesc> games) {
        this.games = games;
    }
}
