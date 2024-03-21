import commands.Command;
import commands.CommandHandler;
import commands.general.HelpCmd;
import commands.general.QuitCmd;
import driver.AppDriver;
import server.ServerFacade;
import state.AppState;

public class Main {
    public static void main(String[] args) {
        CommandHandler handler = new CommandHandler();
        handler.add(new Command(new String[] {"help", "h", "?"}, new HelpCmd()));
        handler.add(new Command(new String[] {"quit", "exit"}, new QuitCmd()));
        AppState state = new AppState(handler, new ServerFacade());
        AppDriver driver = new AppDriver(state);
        driver.run();
    }
}