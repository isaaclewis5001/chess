package commands;

import state.AppState;

public interface CommandEndpoint {
    String[] argumentNames();
    String getDescription();
    void handle(AppState state, String[] inputs) throws CommandHandler.BadContextException;
    boolean validInContext(AppState state);
}
